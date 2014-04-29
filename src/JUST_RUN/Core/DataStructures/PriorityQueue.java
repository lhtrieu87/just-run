/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 * Edited :
 */
package JUST_RUN.Core.DataStructures;

/*
 * Using Double Circular Linked List
 */

public class PriorityQueue<A>
{
    DLinkedList<A> last;
    
    public PriorityQueue()
    {
        last = null;
    }
    
    public boolean isEmpty()
    {
        return last == null;
    }
    
    public void enqueue(int priority, A item)
    {
        DLinkedList<A> node = new DLinkedList<A>(priority, item, null, null);
        if(isEmpty())
        {
            node.next = node;
            node.previous = node;
            last = node;
        }
        else
        {
            // insert according priority
            if(priority >= last.priority || priority < last.next.priority)
            {
                node.previous = last;
                node.next = last.next;
                last.next.previous = node;
                last.next = node;
                if(priority >= last.priority)
                    last = node;
            }
            else
            {
                for(DLinkedList<A> temp = last.previous; temp != last; temp = temp.previous)
                {
                    if(priority >= temp.priority)
                    {
                        node.previous = temp;
                        node.next = temp.next;
                        temp.next.previous = node;
                        temp.next = node;
                        break;
                    }
                }
            }
        }
    }
    
    public DLinkedList<A> dequeue() throws EmptyQueue
    {
        if(!isEmpty())
        {
            DLinkedList<A> node = last.next;
            if(node == last)
                last = null;
            else
            {
                last.next = last.next.next;
                last.next.previous = last;
            }
            return node;
        }
        throw new EmptyQueue("Unhandled Empty Queue Exception found : dequeue");
    }
    
    public void merge(PriorityQueue<A> q)
    {
        for(DLinkedList<A> node = q.last.next; node != q.last; node = node.next)
        {
            this.enqueue(node.priority, node.item);
        }
        this.enqueue(q.last.priority, q.last.item);
    }

    public String toString()
    {
        if(!isEmpty())
        {
            String str = "";
            for(DLinkedList<A> node = last.next; node != last; node = node.next)
            {
                str += node.item + " ( " + node.priority + " ) " + " <-> ";
            }
            return "Queue = " + str + last.item + " ( " + last.priority + " ) ";
        }
        else
            return "Empty Queue";
    }
}
