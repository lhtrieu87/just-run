/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 * Edited :
 */
package JUST_RUN.Core.DataStructures;

/*
 * Using Single Circular Linked List
 */

public class Queue<A>
{
    private SLinkedList<A> last;    

    public Queue()
    {
        last = null;
    }
    
    public void Clear()
    {
    	last = null;
    }
    
    public boolean isEmpty()
    {
        return last == null;
    }
    
    public void Enqueue(A item)
    {
        SLinkedList<A> node = new SLinkedList<A>(item, null);
        if(isEmpty())
        {
            node.next = node;
        }
        else
        {
            node.next = last.next;
            last.next = node;
        }
        last = node;
    }
    
    public void CutFrontQueue(A item)
    {
        SLinkedList<A> node = new SLinkedList<A>(item, null);
        if(!isEmpty())
        {
            node.next = last.next;
            last.next = node;
        }
        else
        {
            Enqueue(item);
        }
    }
    
    public A Dequeue() throws EmptyQueue
    {
        if(!isEmpty())
        {
            SLinkedList<A> node = last.next;
            if(node == last)
                last = null; // special case, when the queue has only one element
            else
                last.next = node.next; // advance to next element
            return node.item;
        }
        
        throw new EmptyQueue("Unhandled Empty Queue Exception found : dequeue operation");
    }
    
    public void Merge(Queue<A> q)
    {
        for(SLinkedList<A> node = q.last.next; node != q.last; node = node.next)
        {
            this.Enqueue(node.item);
        }
        this.Enqueue(q.last.item);
    }
    
    public A Peek() throws EmptyQueue
    {
        if(!isEmpty())
        {
            return last.next.item;
        }
        
        throw new EmptyQueue("Unhandled Empty Queue Exception found : first operation");
    }
    
    public String toString()
    {
        if(!isEmpty())
        {
            String str = "";
            for(SLinkedList<A> node = last.next; node != last; node = node.next)
            {
                str += node.item + " - > ";
            }
            return "Queue = " +  str + last.item;
        }
        else
            return "Empty Queue";
    }
}
