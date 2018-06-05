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

		if(args.length < 1) {
			System.err.println("Arquivo não especificado." + System.lineSeparator()
					+ "USO:" + System.lineSeparator()
					+ "java -jar custo.jar nome_do_arquivo");
			return;
		}

		//Path path = Paths.get(System.getProperty("user.dir"), "data", "caso1000.txt");
		Path path = Paths.get(args[0]);
		calculate(path);
	}

	public static void calculate(Path path) {
		System.out.println("Arquivo:     " + path.getFileName().toString());
		long startTime = System.nanoTime();
		try(BufferedReader file = Files.newBufferedReader(path)) {
			DirectedEdgeWeightedGraph graph = null;
			String line;
			boolean firstLine = false;
			while ((line = file.readLine()) != null) {
				if (line.matches("[0-9]+")) {
					int n = Integer.parseInt(line);
					if (!firstLine) {
						firstLine = true;
						graph = new DirectedEdgeWeightedGraph(n);
					}
				} else {
					Pattern patternNode = Pattern.compile("^([A-Z]+) ([0-9]+)");
					Pattern patternEdge = Pattern.compile("([A-Z]+) ([A-Z]+) ([0-9]+)");
					Matcher matcher = patternNode.matcher(line);
					if (matcher.find()) {
						graph.addVertex(matcher.group(1), Integer.parseInt(matcher.group(2)));
					} else {
						Matcher matcherEdge = patternEdge.matcher(line);
						if (matcherEdge.find()) {
							graph.addEdge(matcherEdge.group(1), matcherEdge.group(2), Integer.parseInt(matcherEdge.group(3)));
						}
					}
				}
			}

			long endTime = System.nanoTime();
			System.out.println("Custo total: " + graph.getTotalCost());
			System.out.println("Tempo:       " + formatElapsedTime(endTime - startTime));
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String formatElapsedTime(long nanoSeconds) {
		long divisor = 1000000000;
		long h = nanoSeconds / divisor / 3600 % 24;
		long m = nanoSeconds / divisor / 60 % 60;
		long s = nanoSeconds / divisor % 60;

		return String.format("%02d:%02d:%02d.%d", h, m, s, nanoSeconds % divisor);
	}
}
