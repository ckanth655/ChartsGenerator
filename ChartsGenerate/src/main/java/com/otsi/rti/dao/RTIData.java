package com.otsi.rti.dao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.otsi.rti.ElasticSearchConnection;
import com.otsi.rti.bean.ElasticData;

import com.otsi.rti.bean.Schema;

public class RTIData {

	static TransportClient client;
	static SearchResponse sr;
	/*
	 * static String index = ElasticSearchConnection.index; static String type =
	 * ElasticSearchConnection.type;
	 */
	static {

		try {
			client = ElasticSearchConnection.connection();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] ags) throws InterruptedException, ExecutionException, IOException {

		RTIData rd = new RTIData();

		System.out.println(rd.getFields("bank"));

	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static List<String> getList(String fieldName, Map<String, Object> mapProperties) {
		List<String> fieldList = new ArrayList<String>();
		Map<String, Object> map = (Map<String, Object>) mapProperties.get("properties");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (((Map<String, Object>) map.get(key)).containsKey("type")) {
				fieldList.add(fieldName + "" + key);
			} else {
				List<String> tempList = getList(fieldName + "" + key + ".", (Map<String, Object>) map.get(key));
				fieldList.addAll(tempList);
			}
		}
		return fieldList;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getIndices() {

		JSONArray list = new JSONArray();

		IndicesStatsResponse stats = client.admin().indices().prepareStats("*").execute().actionGet();
		Map<String, IndexStats> sta = stats.getIndices();
		for (Entry<String, IndexStats> entry : sta.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getFields(String index) throws IOException {

		// System.out.println(index);
		// List<String> fieldList = new ArrayList<String>();
		JSONArray list = new JSONArray();
		ClusterState cs = client.admin().cluster().prepareState().setIndices(index).execute().actionGet().getState();
		IndexMetaData imd = cs.getMetaData().index(index);

		// MappingMetaData mdd = imd.mapping(type);

		ImmutableOpenMap<String, MappingMetaData> types = imd.getMappings();

		String type = null;

		for (ObjectObjectCursor<String, MappingMetaData> ooc : types) {
			type = ooc.key;
		}

		if (type != null && StringUtils.isNotEmpty(type)) {

			MappingMetaData mapping = imd.mapping(type).get();
			Object map = mapping.sourceAsMap().get("properties");
			Map<String, Object> data = (Map<String, Object>) map;

			Set<String> fields = data.keySet();

			for (String field : fields) {
				if(!field.equalsIgnoreCase("@timestamp") && !field.equalsIgnoreCase("@version") && !field.equalsIgnoreCase("@version.original")){
					Map<String, Object> td = (Map<String, Object>) data.get(field);

					// list.addAll(getColumns(td.keySet(),field,td));

					List<Schema> schemaList = getColumns(td.keySet(), field, td);
					for (Schema schema : schemaList) {
						list.add(schema.getField());
					}
				}
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Schema> getColumns(Set<String> fields, String fieldName, Map<String, Object> data) {
		List<Schema> slist = new ArrayList<>();
		for (String field : fields) {
			if (field.equalsIgnoreCase("type")) {
				Schema thisSchema = new Schema();
				String dataType = data.get(field).toString();
				if (!fieldName.contains("fields")) {
					thisSchema.setField(fieldName.replace(".type.", "."));
					thisSchema.setType(dataType);
					slist.add(thisSchema);
				}
			} else {
				if (field.equalsIgnoreCase("fields")) {
					// Schema thisSchema = new Schema();
					Map<String, Object> tdata = (Map<String, Object>) data.get(field);
					Set<String> dfields = tdata.keySet();
					for (String dfield : dfields) {
						if (!fieldName.replace(".type.", "").replace(".type", "").equals(dfield)) {
							Map<String, Object> td = (Map<String, Object>) tdata.get(dfield);
							slist.addAll(getColumns(td.keySet(), fieldName + "." + dfield, td));
						}
					}
				}
			}

		}
		return slist;
	}

	@SuppressWarnings("unchecked")
	public JSONArray queryExecution(ElasticData ed, Map<String, String> queryFields) {

		JSONArray list = new JSONArray();

		String[] indices = ed.getIndices();
		String[] fields = ed.getFields();
		String[] chartTypes = ed.getChartType();

		for (int in = 0; in < indices.length; in++) {
			JSONObject member = new JSONObject();

			String index = indices[in];
			String field = fields[in];
			String chartType = chartTypes[in];

			SearchRequestBuilder search = client.prepareSearch(index).setFrom(0).setSize(10);

			BoolQueryBuilder queryBool = QueryBuilders.boolQuery();

			if (queryFields != null) {

				Set<String> fieldData = queryFields.keySet();

				for (String drillField : fieldData) {

					String selectedField = queryFields.get(drillField);

					if (selectedField != null) {
						
					//	System.out.println(selectedField+"::"+drillField);
						
						queryBool.must(QueryBuilders.matchQuery(selectedField, drillField));
					}

				}
			} else {
				queryBool.should(QueryBuilders.matchAllQuery());
				
			}
			search.setQuery(queryBool);

			search.addAggregation(AggregationBuilders.terms("by_" + field).field(field));

			sr = search.execute().actionGet();

			member.put("index", index);
			member.put("chartType", chartType);
			member.put("field", fields[in]);
			member.put("agg", aggregateData(fields[in]));

			list.add(member);
		}

		return list;

	}

	@SuppressWarnings("unchecked")
	public JSONArray aggregateData(String field) {

		JSONArray al = new JSONArray();

		Terms ts = sr.getAggregations().get("by_" + field);
		al.add(JsonGen.jsonObj(ts));

		// System.out.println(al.toString());

		return al;// .toString();
	}

}
