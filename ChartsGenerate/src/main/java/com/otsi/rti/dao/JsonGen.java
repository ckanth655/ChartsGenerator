package com.otsi.rti.dao;


import java.util.Collection;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class JsonGen{
	
	@SuppressWarnings("unchecked")
	public static JSONArray jsonObj(Terms terms){
		JSONArray array = new JSONArray();

		Collection<Terms.Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {

			JSONObject member = new JSONObject();

			member.put("key", bucket.getKey());
			member.put("value", bucket.getDocCount());

			array.add(member);

		}
		return array;

	}

}
