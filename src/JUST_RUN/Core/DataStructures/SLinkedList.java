/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 * Edited :
 */
package JUST_RUN.Core.DataStructures;

/*
 * Single Linked List
 */
public class SLinkedList <A> 
{
    SLinkedList<A> next;
    A item;
    public SLinkedList(A item, SLinkedList<A> next)
    {
        this.item = item;
        this.next = next;
    }
}
