import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Information of state
 * @author moeka
 *
 */
public class State {
	public int num;
	public HashMap<Integer,String> map;
	public List<Integer> keys;

	State(){
		map = new HashMap<Integer,String>();
		keys = new ArrayList<Integer>();
	}
}
