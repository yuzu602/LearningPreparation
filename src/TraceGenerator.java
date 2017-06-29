import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for trace generation
 *
 * @author moeka
 *
 */
public class TraceGenerator {

	// Transition description of environment model (envModel_small/large.txt)
	private static String transitionsName = "envModel_large.txt";

	// Name of output file
	private static String traceName = "Trace.txt";

	// Length of generated trace
	private static int traceSize = 10000;

	private static int stateSize;
	private static List<State> states;

	public static void main(String[] args) {
		ExtractTransition();

		Generate(false);
	}

	/**
	 * Extract transitions from transition description of environment model
	 */
	public static void ExtractTransition() {
		String[] line = new String[5];
		State s;
		states = new ArrayList<State>();
		int m1;
		String m2;

		try {
			File file = new File(transitionsName);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str;
			for (int i = 0; i < 3; i++) {
				br.readLine();
			}
			str = br.readLine();
			stateSize = Integer.parseInt(str);

			for (int i = 0; i < 2; i++) {
				br.readLine();
			}
			while ((str = br.readLine()) != null) {
				line = new String[5];
				line = str.split(" ", 0);

				if (line[0].startsWith("Q")) {
					s = new State();
					s.num = states.size();
					states.add(s);

					if (line[line.length - 1].endsWith(",") || line[line.length - 1].endsWith(".")) {
						m1 = Integer.parseInt(line[3].substring(1, line[3].length() - 2));
						m2 = line[1].substring(2 - 1);
						states.get(states.size() - 1).map.put(m1, m2);
						states.get(states.size() - 1).keys.add(m1);
					} else {
						m1 = Integer.parseInt(line[3].substring(2 - 1));
						m2 = line[1].substring(2 - 1);
						states.get(states.size() - 1).map.put(m1, m2);
						states.get(states.size() - 1).keys.add(m1);
					}
				} else if (line[line.length - 1].endsWith(",") || line[line.length - 1].endsWith(".")) {
					m1 = Integer.parseInt(line[4].substring(1, line[4].length() - 2));
					m2 = line[2].substring(2 - 1);
					states.get(states.size() - 1).map.put(m1, m2);
					states.get(states.size() - 1).keys.add(m1);
				} else {
					m1 = Integer.parseInt(line[4].substring(2 - 1));
					m2 = line[2].substring(2 - 1);
					states.get(states.size() - 1).map.put(m1, m2);
					states.get(states.size() - 1).keys.add(m1);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}


	/**
	 * Generate trace file
	 *
	 * @param random
	 *            When two post-condition are observed, true.
	 *            When only one post-condition is observed, false.
	 *
	 */
	public static void Generate(boolean random) {
		int count = 1;
		Random rnd = new Random();
		int r;
		int stateNum;
		State s;
		String pre = "null", prepre = "null";
		int rr;

		try {
			File file = new File(traceName);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			stateNum = states.get(0).keys.get(0);

			while (count <= traceSize + 1) {
				s = states.get(stateNum);
				r = rnd.nextInt(s.keys.size());

				// for kiva...
				if (s.map.get(s.keys.get(r)).equals("start")
						|| s.map.get(s.keys.get(r)).equals("wait")) {
					count--;
					stateNum = s.keys.get(r);
				} else if (random == false) {
					// When only one post-condition is observed
					pw.println(s.map.get(s.keys.get(r)));
					stateNum = s.keys.get(r);
				} else if (transitionsName.equals("envModel_small.txt")){
					// When two post-conditions are observed in small case
					if(prepre.startsWith("arrive")){
						rr = rnd.nextInt(2);
						if (rr == 0) {
							pw.println(s.map.get(s.keys.get(r)));
							stateNum = s.keys.get(r);
							prepre = pre;
							pre = s.map.get(s.keys.get(r));
						} else {
							pw.println(prepre);
							count++;
							pw.println(pre);
						}
					}else{
						pw.println(s.map.get(s.keys.get(r)));
						stateNum = s.keys.get(r);
						prepre = pre;
						pre = s.map.get(s.keys.get(r));
					}
				} else {
					// When two post-conditions are observed in large case
					if (pre.equals("move.s") || pre.equals("move.n")
							|| pre.equals("move.e") || pre.equals("move.w")) {
						if (prepre.startsWith("arrive[")) {
							rr = rnd.nextInt(2);
							if (rr == 0) {
								pw.println(s.map.get(s.keys.get(r)));
								stateNum = s.keys.get(r);
								prepre = pre;
								pre = s.map.get(s.keys.get(r));
							} else {
								pw.println(prepre);
								count++;
								pw.println(pre);
							}
						} else {
							pw.println(s.map.get(s.keys.get(r)));
							stateNum = s.keys.get(r);
							prepre = pre;
							pre = s.map.get(s.keys.get(r));
						}
					} else {
						pw.println(s.map.get(s.keys.get(r)));
						stateNum = s.keys.get(r);
						prepre = pre;
						pre = s.map.get(s.keys.get(r));
					}
				}
				count++;
			}
			pw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}
