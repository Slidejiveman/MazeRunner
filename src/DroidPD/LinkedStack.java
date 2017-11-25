package DroidPD;

public class LinkedStack<E> implements Stack<E> {
	/**--------Create Empty List--------*/
	private SinglyLinkedList<E> list = new SinglyLinkedList<E>();
    /**--------Constructor--------*/
	public LinkedStack() {
		
	}
	/**--------Interface Methods--------*/
	public int size() {
		return this.list.size();
	}
	public boolean isEmpty() {
		return this.list.isEmpty();
	}
	public void push(E e) {
		this.list.addFirst(e);
	}
	public E top() {
		return this.list.first();
	}
	public E pop() {
		return this.list.removeFirst();
	}
}
