package Unit;

import java.util.PriorityQueue;

import org.junit.Test;

import com.lsj.util.BinarySearchTreeMap;
import com.lsj.util.LArrayList;
import com.lsj.util.LLinkedList;
import com.lsj.util.LList;
import com.lsj.util.LMap;

public class Demo {

	//@Test
	public void testLArrayList() {
		LList<Integer> list = new LArrayList<>();
		for(int i=0; i<50; i++){
			list.add(i);
		}
		
		list.remove(20);
		list.remove(new Integer(30));
		System.out.println(list);
	}
	
	//@Test
	public void testLLinkedList() {
		LList<Integer> list = new LLinkedList<>();
		for(int i=0; i<50; i++){
			list.add(i);
		}
		
		list.remove(0);
		list.remove(new Integer(10));
		System.out.println(list);
	}
	
	//@Test
	public void testBST() {
		LMap<Integer, String> map = new BinarySearchTreeMap<>();
		map.put(-2, "lsj");
		map.put(-1, "18");
		map.put(0, "man");
		map.put(1, "lsj");
		map.put(2, "18");
		
		System.out.println(map.get(-2));
		System.out.println(map.get(-1));
		System.out.println(map.get(0));
		System.out.println(map.get(1));
		System.out.println(map.get(2));
	}
	
	@Test
	public void test() {
	}
}
