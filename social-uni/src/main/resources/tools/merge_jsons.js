var args = process.argv.slice(2);
if (args.length < 2) {
	console.log('Usage: node merge_jsons.js json1 json2 ...');
}
var fs = require('fs');
var merged = [].concat.apply([], args.map(file => JSON.parse(fs.readFileSync(file))));
console.log(JSON.stringify(merged, null, 4));