package com.otsi.rti.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import com.otsi.rti.bean.ElasticData;
import com.otsi.rti.dao.RTIData;

/**
 * Servlet implementation class RTIDataAgg
 */
public class RTIDataAgg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RTIDataAgg() {
		super();
		queryFields = new HashMap<String, String>();
	}

	@Override
	public void init() throws ServletException {

		// queryFields=new HashMap<String,String>();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	Map<String, String> queryFields;// =new HashMap<String,String>();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = null;

		// Map<String,String> queryFields=new HashMap<String,String>();

		pw = response.getWriter();

		String[] fields = request.getParameterValues("fields[]");
		String[] indices = request.getParameterValues("indices[]");
		String[] chartTypes = request.getParameterValues("chartTypes[]");
		String fieldData = request.getParameter("drill");
		String selectedField = request.getParameter("field");

		ElasticData ed = new ElasticData();
		ed.setIndices(indices);
		ed.setFields(fields);
		ed.setChartType(chartTypes);

		if (fieldData != null && selectedField != null) {
			queryFields.put(fieldData, selectedField);
			 System.out.println(queryFields.toString());
		}else{
			System.out.println(fieldData+"\t"+selectedField);
			queryFields.clear();
			System.out.println(queryFields);
		}
		

		RTIData rti = new RTIData();

		JSONArray list = rti.queryExecution(ed, queryFields);

		pw.print(list.toJSONString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
