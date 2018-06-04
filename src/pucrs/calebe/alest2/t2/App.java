package pucrs.calebe.alest2.t2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

	public static void main(String[] args) {
		Path path = Paths.get(System.getProperty("user.dir"), "data", "caso0100.txt");
		try(BufferedReader file = Files.newBufferedReader(path)) {
			DirectedEdgeWeightedGraph graph = null;
			String line;
			boolean firstLine = false;
			while((line = file.readLine()) != null) {
				if(line.matches("[0-9]+")) {
					int n = Integer.parseInt(line);
					if(!firstLine) {
						firstLine = true;
						graph = new DirectedEdgeWeightedGraph(n);
					} else {
						//
					}
				} else {
					Pattern patternNode = Pattern.compile("^([A-Z]+) ([0-9]+)");
					Pattern patternEdge = Pattern.compile("([A-Z]+) ([A-Z]+) ([0-9]+)");
					Matcher matcher = patternNode.matcher(line);
					if(matcher.find()) {
						graph.addNode(matcher.group(1), Integer.parseInt(matcher.group(2)));
					} else {
						Matcher matcherEdge = patternEdge.matcher(line);
						if(matcherEdge.find()) {
							graph.addEdge(matcherEdge.group(1), matcherEdge.group(2), Integer.parseInt(matcherEdge.group(3)));
						}
					}
				}
			}
			
			System.out.println(graph);
			System.out.println(graph.toGraphViz());

			//System.out.println(graph.getTotalCost());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
