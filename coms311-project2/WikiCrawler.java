// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may only include libraries of the form java.*)


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Cody Lakin (calakin)
 * @author Logan Kinneer (lkinneer)
 *
 */
public class WikiCrawler {
	static final String BASE_URL = "https://en.wikipedia.org";

	private int max;
	private String seedUrl;
	private String fileName;
	private ArrayList<String> topics;

	/**
	 * This keeps track of connections made to the domain since the last wait
	 * period. To adhere to a politeness policy, when the count reaches 25 the
	 * thread will wait 3 seconds, then reset the count and resume the program.
	 */
	private int streamsOpened;

	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName) {
		this.max = max;
		this.seedUrl = seedUrl;
		this.fileName = fileName;
		this.topics = topics;
		this.streamsOpened = 0;
	}

	public void crawl() {

		File outFile = new File(fileName);
		PrintWriter out = null;

		// create a file if non-existent, create PrintWriter for that file
		try {
			if (!outFile.exists()) {
				outFile.createNewFile();
			}

			out = new PrintWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// output the max number of pages to visit
		out.println(max);
		out.flush();

		// If max is zero, the graph will be empty. Stop here.
		if (max <= 0) {
			out.close();
			return;
		}
		
		String htmlString = requestWebpage(BASE_URL + seedUrl);
		
		streamsOpened++;
		
		if (htmlString.length() == 0 || !containsTopics(htmlString, topics) ) {
			out.close();
			return;
		}

		// initialize count of output graph edges, queue of edges, and visited edges
		int count = 1;
		Queue<Vertex> Q = new LinkedList<Vertex>();
		
		//contains all values of nodes that have been checked for topics and had links added
		HashMap<Integer, String> visited = new HashMap<Integer, String>(max );
		
		// contains unique nodes that contain the topics, contains max of them
		HashMap<Integer, String> graph = new HashMap<Integer, String>(max);

		visited.put(seedUrl.hashCode(), seedUrl);
		graph.put(seedUrl.hashCode(), seedUrl);
		Q.add(new Vertex(seedUrl, htmlString));

		while (!Q.isEmpty()) {
			Vertex currentVertex = Q.poll();

			//get the document in string form
			htmlString = currentVertex.getHtml();

			streamsOpened++;

			// check for politeness policy pause
			if (streamsOpened == 25) {
				try {
					streamsOpened = 0;
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// get links from current webpage
			ArrayList<String> links = extractLinks(htmlString);

			//for each link from the webpage
			int i, length = links.size();
			ArrayList<String> linksVisited = new ArrayList<String>();
			linksVisited.add(currentVertex.getUrl());
			for (i = 0; i < length; i++) {
				String current = links.get(i);
				int key = current.hashCode();
				
				if( linksVisited.contains(current) ) {
					continue;
				}
				
				linksVisited.add(current);
				
				if ( !(graph.containsKey(key) && graph.get(key).equals(current)) ) {
					if (count >= max) {
						continue;
					}
				}

				htmlString = requestWebpage(BASE_URL + current);

				if (htmlString.length() == 0) {
					continue;
				}

				streamsOpened++;

				// check for politeness policy pause
				if (streamsOpened == 25) {
					try {
						streamsOpened = 0;
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (containsTopics(htmlString, topics)) {
					if ( !(graph.containsKey(key) && graph.get(key).equals(current)) ) {
						if (count < max) {
							count++;
						} else {
							continue;
						}
					}
					
					// output current edge to the file
					graph.put(key, current);
					out.println(currentVertex.getUrl() + " " + current);
					out.flush();

					if (!(visited.containsKey(key) && visited.get(key).equals(current))) {
						Q.add(new Vertex(current, htmlString));
						visited.put(key, current);
					}
				}
			}
		}
		out.close();
	}

	/*
	 * Turns the webpage retrieved from the given url into a string.
	 */
	private static String requestWebpage(String urlString) {
		String document = "";

		try {
			URL url = new URL(urlString);
			InputStream in = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line = br.readLine();
			while (line != null) {
				document += line;
				document += '\n';
				line = br.readLine();
			}

			br.close();

			int i, length = document.length();
			for (i = 0; i < length - 3; i++) {
				if (document.charAt(i) == '<') {
					if (document.charAt(i + 1) == 'p') {
						if (document.charAt(i + 2) == '>') {
							document = document.substring(i + 3, document.length() - 1);
							return document;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	// given an html string, this function will check if it contains the given list
	// of key words. Iff, it contains them all, it will return true.
	private static boolean containsTopics(String webpage, ArrayList<String> topics) {
		if (topics.size() == 0) {
			return true;
		}

		// get the largest string size in topics
		String largest = "";
		int i, length = topics.size();
		for (i = 0; i < length; i++) {
			if (largest.length() < topics.get(i).length()) {
				largest = topics.get(i);
			}
		}

		// First index refers to a set of hash values for shingle lengths of that index
		// Second index refers to a particular shingle's hash value within a string
		// Used to reduce use of rollingHash() when there are multiple topics
		int[][] hashCodes = new int[largest.length() + 1][];

		for (i = 0; i < length; i++) {
			int sLength = topics.get(i).length();
			int mod = 256;

			if (hashCodes[sLength].length == 0) {
				hashCodes[sLength] = rollingHash(webpage, sLength, mod);
			}

			int hashCode = (int) (Math.abs(stringHash(topics.get(i)) % mod));
			boolean found = false;

			int j, numHashCodes = hashCodes[sLength].length;
			for (j = 0; j < numHashCodes; j++) {
				if (hashCodes[sLength][j] == hashCode) {
					String s1 = webpage.substring(j, j + sLength);
					String s2 = topics.get(i);

					if (s1.equals(s2)) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				return false;
			}

			found = false;
		}
		return true;
	}

	/*
	 * This function returns the links included in an html webpage. Only the links
	 * in the actual text component, after the first <p>, are returned. Links are
	 * indexed in the order that they appear in the document, from 0 onward. Only
	 * pages from the domain https://en.wikipedia.org are included. Only links of
	 * the form "/wiki/xxx" are extracted. No links containing ':' or '#' are
	 * explored.
	 */
	private static ArrayList<String> extractLinks(String webpage) {
		ArrayList<String> links = new ArrayList<String>();

		int i, length = webpage.length();
		for (i = 0; i < length - 4; i++) {
			if (webpage.charAt(i) == '<' && webpage.charAt(i + 1) == 'a' && webpage.charAt(i + 2) == ' ') {
				i = i + 4;

				// find the string 're*="' within an <a> element
				// if the third character is not f, break and href = false
				while (i < length - 4 && webpage.charAt(i) != 'r' && webpage.charAt(i + 1) != 'e'
						&& webpage.charAt(i + 3) != '=' && webpage.charAt(i + 4) != '\"') {
					i++;
				}

				// it is not a link, go to the next iteration
				if (webpage.charAt(i + 2) != 'f') {
					continue;
				}

				// grab the url string
				i = i + 5;
				int urlStart = i;
				while (webpage.charAt(i) != '\"' || (webpage.charAt(i - 1) == '\\' && webpage.charAt(i) == '\"')) {
					i++;
				}

				String url = webpage.substring(urlStart, i);

				// do not include urls with unwanted qualities
				if (url.contains("#") || url.contains(":") || url.length() < 6
						|| !url.substring(0, 6).equals("/wiki/")) {
					continue;
				} else {
					links.add(url);
				}
			}
		}

		return links;
	}

	// Gets a string s, returns an int array. The int array represents hash code for
	// each possible combination of characters, in order. The combinations are of a
	// given
	// length, sLength.
	private static int[] rollingHash(String s, int sLength, int m) {
		int numShingles = s.length() - sLength + 1;
		double[] set1 = new double[numShingles];
		int[] set2 = new int[numShingles];

		set1[0] = stringHash(s.substring(0, sLength));
		set2[0] = Math.abs((int) (set1[0] % m));

		int i;
		for (i = 1; i < numShingles; i++) {
			char prev = s.charAt(i - 1);
			char cur = s.charAt(i + sLength - 1);

			set1[i] = ((set1[i - 1] - prev * Math.pow(31, sLength - 1)) * 31 + (cur + 0));
			set2[i] = (int) (set1[i] % m);
		}

		return set2;
	}

	private static double stringHash(String s) {
		double sum = 0;

		int i, length = s.length();
		for (i = 0; i < length; i++) {
			sum += s.charAt(i) * Math.pow(31, length - 1 - i);
		}

		return sum;
	}
}

/*
 * This container class stores a graph edge between two URLs. The graph edge
 * represents a link.
 */
class Vertex {
	private String htmlString;
	private String url;

	public Vertex(String url, String htmlString) {
		this.htmlString = htmlString;
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}
	
	public String getHtml() {
		return this.htmlString;
	}
}