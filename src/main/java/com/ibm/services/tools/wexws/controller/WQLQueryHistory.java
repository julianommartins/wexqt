package com.ibm.services.tools.wexws.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WQLQueryHistory {

	private static WQLQueryHistory instance;
	private List<String> historyList;
	private final Object lock = new Object();

	private WQLQueryHistory() {
		this.historyList = loadHistorylist();
	}

	public static WQLQueryHistory getInstance() {
		if (instance == null) {
			instance = new WQLQueryHistory();
		}
		return instance;
	}

	public List<String> getHistoryList() {
		return historyList;
	}

	private List<String> loadHistorylist() {
		List<String> list = new ArrayList<String>();
		synchronized (lock) {
			try {
				String content;

				content = loadFile("history.data");

				for (String wql : content.split("</wql>")) {
					if (wql.trim().length() > 0) {
						wql = wql.replace("<wql>", "").replace("</wql>", "");
						while (wql.indexOf("\n\n") > -1) {
							wql = wql.replace("\n\n", "\n");
						}
						if (!list.contains(wql)) {
							list.add(wql);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	public void updateHistory() {

		synchronized (lock) {
			try {
				FileWriter fw = new FileWriter("history.data");

				for (String wql : this.historyList) {

					if (!"*deleted*".equals(wql) && wql.trim().length() > 0) {

						fw.write("<wql>");
						fw.write(wql);
						fw.write("</wql>\n");

					}

				}
				fw.flush();
				fw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			this.historyList = loadHistorylist();

		}

	}

	public void removeWQLFromHistory(int index) {
		this.historyList.set(index, "*deleted*");
	}

	public void saveHistory(String wql) {

		if (!wql.startsWith("--"))
			return;

		if (this.historyList.contains(wql))
			return;

		this.historyList.add(wql);

		synchronized (lock) {

			try {
				FileWriter fw = new FileWriter("history.data", true);
				fw.write("<wql>");
				fw.write(wql);
				fw.write("</wql>\n");
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private String loadFile(String filename) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		return sb.toString();
	}
}
