package com.ibm.services.tools.wexws.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.services.tools.wexws.utils.HttpRequest;
import com.ibm.services.tools.wexws.utils.XMLUtil;


public class StatusBO {
	// Tem que fazer uma collection por ambiente (Infelizmente)
	private static String[] COLLECTIONS = {
		"PMP_GR_Client_1_1",
		"PMP_JRSS_Collection",
		"PMP_Regular_Client_1_1",
		"PMP_Account_Customer_1_1",
		"PMP_My_Professionals_1_3",
		"PMP_My_Professionals_2_3",
		"PMP_My_Professionals_3_3",
		"PMP_OpenSeat_1_1",
		"PMP_Opportunity_1_4",
		"PMP_Opportunity_2_4",
		"PMP_Opportunity_3_4",
		"PMP_Opportunity_4_4",
		"PMP_Practitioner_1_3",
		"PMP_Practitioner_2_3",
		"PMP_Practitioner_3_3",
		"PMP_StaffingPlan_1_1",
		"PMP_Utilization_1_1",
	};
	
	private static String[] COLLECTIONS_SYSTEST = {
		"PMP_GR_Client_1_1",
		"PMP_JRSS_Collection",
		"PMP_Regular_Client_1_1",
		"PMP_Account_Customer_1_1",
		"PMP_My_Professionals_1_3",
		"PMP_My_Professionals_2_3",
		"PMP_My_Professionals_3_3",
		"PMP_OpenSeat_1_1",
		"PMP_Opportunity_1_2",
		"PMP_Opportunity_2_2",
		"PMP_Practitioner_1_3",
		"PMP_Practitioner_2_3",
		"PMP_Practitioner_3_3",
		"PMP_StaffingPlan_1_1",
		"PMP_Utilization_1_1",
	};
	
	private static String[] COLLECTIONS_SBOX = {
			"PMP_GR_Client_1_1",
			"PMP_JRSS_Collection",
			"PMP_Regular_Client_1_1",
			"PMP_Account_Customer_1_1",
			"PMP_My_Professionals_1_1",
			"PMP_OpenSeat_1_1",
			"PMP_Opportunity_1_3",
			"PMP_Opportunity_2_3",
			"PMP_Opportunity_3_3",
			"PMP_Practitioner_1_1",
			"PMP_StaffingPlan_1_1",
			"PMP_Utilization_1_1",
		};
	
	private static String STATUS_URL = "http://%host%:9080/vivisimo/cgi-bin/velocity?v.function=search-collection-status&v.indent=true&collection=%collection%&v.username=api-user&v.password=pmpDEapi&v.app=api-rest";

	public String getStatus(String environment) {
		StringBuilder sb = new StringBuilder();
		String hosts = getHost(environment);
		
		sb.append("<h3>Get collection status</h3>Environment: ").append(environment).append("<br>\n");
		if(hosts!=null){
		  sb.append("Hosts: ").append(hosts).append("<br>\n");
		}
		
		if(hosts==null) return "No host found for env="+environment+"\n valid env values: dev,systest,prodval,ivt,prod,sbox";
		
		for(String host : hosts.split(",")){
		
			sb.append("<hr>");
			sb.append("HOST:"+host+"<br>\n");
			
			String url = STATUS_URL.replace("%host%", host);
			
			String[] COLLECTIONS_USED = COLLECTIONS; 
			if (environment.equalsIgnoreCase("systest")){
				COLLECTIONS_USED = COLLECTIONS_SYSTEST;
			}
			
			for(String collection : COLLECTIONS_USED){
				
				String query = url.replace("%collection%", collection);
				String xml = new HttpRequest(query, null, false).doGet();
				
				boolean isMerging = false;
				if (xml.contains("merge=\"active\"")){
					isMerging = true;
				}
				
				String nThreads = "NA";
				List<String> indexedFields = XMLUtil.getChunks(xml, "<vse-serving " ,"/>",true);
				for(String chunk : indexedFields){
					nThreads = XMLUtil.getChunk(chunk, "n-threads=\"" ,"\"");
				}
				
				List<String> list = XMLUtil.getChunks(xml, "<crawl-remote-all-status>" ,"</crawl-remote-all-status>",true);
				for(String status : list){
					sb.append("-------------------------------------------------").append("<br>\n");
					sb.append("<b>").append("<a href=" + query + ">" + collection + "</a>").append("</b><br>\n");
					
					if (isMerging){
						sb.append("<b><font color=\"red\"><b>Collection is Merging</b></font></b><br>\n");
					} else {
						sb.append("<b><font color=\"green\"><b>Collection is not Merging</b></font></b><br>\n");
					}
					sb.append("<b><font color=\"orange\"><b>Index Threads: " + nThreads + "</b></font></b><br>\n");

					status = status.replace("<", "&lt;")
							.replace(">", "&gt;<br>\n")
					.replace("\"synchronized:", "\"<font color=\"green\"><b>synchronized:</b></font>")
					.replace("\"not synchronized:", "\"<font color=\"orange\"><b>not synchronized:</b></font>")
					.replace("synchronizing:", "<font color=\"orange\"><b>synchronizing:</b></font>")
					
					.replace("disconnected", "<font color=\"red\"><b>disconnected</b></font>")
					
					.replace("attempting to connect", "<font color=\"red\"><b>attempting to connect</b></font>")
					;
					sb.append(status).append("<br>");
				}
				
				
			}
		
		}
				
		
		return sb.toString();
	}

	public List<String> getCollectionFields(String environment, String collectionName){
		List<String> fields = new ArrayList<String>();
		
		String hosts = getHostFields(environment);
		String url = STATUS_URL.replace("%host%", hosts);
		
		String[] COLLECTIONS_USED = COLLECTIONS; 
		if (environment.equalsIgnoreCase("systest")){
			COLLECTIONS_USED = COLLECTIONS_SYSTEST;
		}
		if (environment.equalsIgnoreCase("sbox")){
			COLLECTIONS_USED = COLLECTIONS_SBOX;
		}
		
		for(String col : COLLECTIONS_USED){
			if(col.startsWith(collectionName)){
				String query = url.replace("%collection%", col);
				String xml = new HttpRequest(query, null, false).doGet();
				List<String> indexedFields = XMLUtil.getChunks(xml, "<vse-index-content " ,"/>",true);
				for(String chunk : indexedFields){
					String name = XMLUtil.getChunk(chunk, "name=\"" ,"\"");
					name = name.replace("0_", "");
					if(name.length()>0 && name.indexOf("#")==-1 && !fields.contains(name)){
						fields.add(name);
					}
				}
			}
		}
		
		Collections.sort(fields);
		
		return fields;
		
	}
	
	private String getHost(String env){
		if("dev".equals(env)) return "dstvm14g01.boulder.ibm.com,dstvm14g02.boulder.ibm.com";
		if("systest".equals(env)) return "rcmvpmp01.boulder.ibm.com,rcmvpmp02.boulder.ibm.com";
		if("prodval".equals(env)) return "rcmvpmp03.bld.dst.ibm.com,rcmvpmp04.bld.dst.ibm.com";
		if("prod".equals(env)) return "b01n0134.ahe.pok.ibm.com,b01n0135.ahe.pok.ibm.com,b01n0154.ahe.pok.ibm.com,b01n0155.ahe.pok.ibm.com";
		if("sbox".equals(env)) return "rcmblu.bld.dst.ibm.com";
		return null;
	}
	
	private String getHostFields(String env){
		if("dev".equals(env)) return "dstvm14g01.boulder.ibm.com";
		if("systest".equals(env)) return "rcmvpmp01.boulder.ibm.com";
		if("prodval".equals(env)) return "rcmvpmp03.bld.dst.ibm.com";
		if("prod".equals(env)) return "b01n0134.ahe.pok.ibm.com";
		if("sbox".equals(env)) return "rcmblu.bld.dst.ibm.com";
		return null;
	}

}
