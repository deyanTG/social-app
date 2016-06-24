#!/bin/bash

es="http://localhost:9200"

curl -XDELETE "$es/companies"
curl -XDELETE "$es/industries"

#geoLocation as geo_point
curl -XPOST "$es/companies" -d '{ "settings":{
     "index":{
        "analysis":{
           "analyzer":{
              "lowercase_keyword_analyzer":{
                 "tokenizer":"keyword",
                 "filter":"lowercase"
              },
              "email_analyzer":{
              	"tokenizer":"uax_url_email"
              }
           }
        }
     }
    },"mappings":{
	"company": {"properties":{ 
		"address" : {"type" : "nested", "properties" : {"geoLocation":{"type":"geo_point"}}},
		"headquarters" : {"type" : "nested", "properties" : {"geoLocation":{"type":"geo_point"}}}, 
		"description" : {"type":"string", "analyzer":"english"}, 
		"name" : {"type":"string", "analyzer":"english"}, 
		"industries" : {"type":"string", "analyzer": "lowercase_keyword_analyzer", "index_options": "docs"},
		"industries_analyzed" : {"type":"string", "analyzer":"english"}, 
		"specialties" : {"type":"string", "analyzer": "lowercase_keyword_analyzer", "index_options": "docs"},
		"specialties_analyzed" : {"type":"string", "analyzer":"english"} 
		} 
	}, 
	"opportunity": {"properties":{ 
		"address" : {"type" : "nested", "properties" : {"geoLocation":{"type":"geo_point"}}}, 
		"headquarters" : {"type" : "nested", "properties" : {"geoLocation":{"type":"geo_point"}}}, 
		"description" : {"type":"string", "analyzer":"english"}, 
		"name" : {"type":"string", "analyzer":"english"}, 
		"industries" : {"type":"string", "analyzer": "lowercase_keyword_analyzer", "index_options": "docs"},
		"industries_analyzed" : {"type":"string", "analyzer":"english"}, 
		"specialties" : {"type":"string", "analyzer": "lowercase_keyword_analyzer", "index_options": "docs"},
		"specialties_analyzed" : {"type":"string", "analyzer":"english"} 
		} 
	},
	"user": {"properties":{
		"address" : {"type" : "nested", "properties" : {"geoLocation":{"type":"geo_point"}}},
		"firstName" : {"type":"string", "analyzer":"english"},
		"lastName" : {"type":"string", "analyzer":"english"},
		"username" : {"type":"string", "analyzer":"email_analyzer"},
		"bio" : {"type":"string", "analyzer":"english"},
		"position" : {"type":"string", "analyzer":"english"},
		"is_expert": {"type":"boolean"},
		"phone":{"type":"string", "analyzer":"lowercase_keyword_analyzer"},
		"specialties" : {"type":"string", "analyzer": "lowercase_keyword_analyzer", "index_options": "docs"},
		"specialties_analyzed" : {"type":"string", "analyzer":"english"},
		"industries" : {"type":"string", "analyzer": "lowercase_keyword_analyzer", "index_options": "docs"},
		"industries_analyzed" : {"type":"string", "analyzer":"english"}
		}
	}
}}'

curl -XPOST "$es/industries" -d '{"mappings":{
	"industry": {"properties":{ 
		"name":{"type":"string"}, 
		"suggest" : {"type" : "completion", "analyzer" : "simple", "search_analyzer" : "simple", "payloads" : false} 
		} 
	}, 
	"specialty": {"properties":{ 
		"name":{"type":"string"}, 
		"suggest" : {"type" : "completion", "analyzer" : "simple", "search_analyzer" : "simple", "payloads" : false} 
		} 
	} 
}}'

