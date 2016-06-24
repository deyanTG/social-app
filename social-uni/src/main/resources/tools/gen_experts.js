var args = process.argv.slice(2);
if (args.length < 2) {
	console.log('Usage: node gen_experts.js input-dir output-file')
}
var fs = require('fs');
var rootDir = args[0];
var geocoderProvider = 'google';
var httpAdapter = 'https';
var extra = {
    apiKey: 'AIzaSyAE766dD05b-dUaK7ApSNGRBXe_Fd2TsNk'
};
var geocoder = require('node-geocoder')(geocoderProvider, httpAdapter, extra);
var files = fs.readdirSync(rootDir);
var mega_json = [];
var promises = [];
files.forEach(function(file) {
	var personnel = JSON.parse(fs.readFileSync(rootDir + '/' + file)).personnel;
	var promise = new Promise(function(resolve, reject) {
		if (personnel) {
			geocoder.geocode(file.split('.').slice(0, -1).join('.'), function(err, data) {
				var geo_address = data ? data[0] : undefined;
				if (geo_address) {
					var address = {
						city: geo_address.city,
						country: geo_address.country,
						state: geo_address.administrativeLevels.level1long
					};
					personnel = personnel.map(x => { x.address = address; return x; });
				} else {
					console.log('Failed to get address for ' + file);
					console.log(data);
				}
				resolve(personnel);
			});	
		} else {
			resolve([]);
		}
	});
	promises.push(promise);
});
Promise.all(promises).then(function(data) {
	fs.writeFile(args[1], JSON.stringify([].concat.apply([], data), null, 4));
});