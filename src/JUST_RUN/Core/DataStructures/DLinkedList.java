/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 * Edited :
 */
package JUST_RUN.Core.DataStructures;

/*
 * Double Linked List
 */
public class DLinkedList <A>
{
    DLinkedList<A> next, previous;
    int priority;
    A item;
    
    public DLinkedList(A item, DLinkedList<A> next, DLinkedList<A> previous)
    {
        this.item = item;
        this.next = next;
        this.previous = previous;
    }
    
    public DLinkedList(int priority, A item, DLinkedList<A> next, DLinkedList<A> previous)
    {
        this.priority = priority;
        this.item = item;
        this.next = next;
        this.previous = previous;
    }
}
