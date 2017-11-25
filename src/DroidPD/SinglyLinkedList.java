package DroidPD;

public class SinglyLinkedList<String> {
	/**--------Nested Node inner class--------*/
	private static class Node <String> {
		private String element;
		private Node<String> next;
		public Node(String string, Node<String> next) {
			this.element = string;
			this.next = next;
		}
		public String getElement() {
			return this.element;
		}
		public Node<String> getNext() {
			return this.next;
		}
		public void setNext(Node<String> next) {
			this.next = next;
		}
	/**--------End of Nested Node inner class-*/
	}
	/**----SINGLY LINKED LIST variables--------*/
	private Node<String> head = null;
	private Node<String> tail = null;
	private int size = 0;
	/**--------Constructor--------*/
	public SinglyLinkedList() {
		
	}
	/**--------Accessors--------*/
	public int size() {
		return this.size;
	}
	public boolean isEmpty() {
		return (size == 0);
	}
	public String first() {
		if(isEmpty()) {
			return null;
		}
		return this.head.getElement();
	}
	public String last() {
		if(isEmpty()) {
			return null;
		}
		return this.tail.getElement();
	}
	/**--------Updaters--------*/
	public void addFirst(String string) {
		this.head = new Node<String>(string, head);
		if(size == 0) {
			this.tail = head;
		}
		size++;
	}
	public void addLast(String string) {
		Node<String> newest = new Node<String>(string, null);
		if(isEmpty()) {
			this.head = newest;
		} else {
			this.tail.setNext(newest);
		}
		this.tail = newest;
		size++;
	}
	public String removeFirst() {
		if(isEmpty()) {
			return null;
		}
		String answer = this.head.getElement();				//Returns the element removed
		this.head = head.getNext();
		size--;
		if(size == 0) {
			this.tail = null;
		}
		return answer;
	}
	    
	
	
}

