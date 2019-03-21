//Michael Groff
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.lang.*;
import static java.lang.Math.*;

class Node<T extends Comparable<T>>
{
  int height;
  T data;
  ArrayList<Node<T>> Nexts;

  Node(int height)
  {
    this.data = null;
    this.height = height;
    Nexts = new ArrayList<Node<T>>();//arraylist to hold pointers to other nodes for each level
    for(int i =0; i <height; i++)
      Nexts.add(null);//initializing all pointers to null
  }
  Node(T data, int height)
  {
    this.data = data;
    this.height = height;
    Nexts = new ArrayList<Node<T>>();
    for(int i =0; i <height; i++)
      Nexts.add(null);
  }
  public T value()
  {
    return this.data;
  }
  public int height()
  {
    return this.height;
  }
  public Node<T> next(int level)
  {
    if(level > this.height-1 || level < 0)
      return null;
    return this.Nexts.get(level);
  }
  public void setNext(int level, Node<T> node)
  {
    this.Nexts.set(level,node);
  }
  public void grow()
  {
    this.height++;
    this.Nexts.add(null);
  }
  public void maybeGrow()
  {
    Random rand = new Random();
    int  n = rand.nextInt(2);
    if(n==1)
    {
      this.height++;
      this.Nexts.add(null);
    }
  }
  public void trim(int height)
  {
    Node<T> del = new Node<T>(height);
    int l = this.height;
    this.height = height;
    while(l > height)
    {
      del = Nexts.get(l-1);
      Nexts.remove(del);
    }
  }
}

public class SkipList<T extends Comparable<T>>
{
  Node<T> head;
  int height;
  int size;

  SkipList()
  {
    this.height = 1;
    head = new Node<T>(this.height);
    this.size = 0;
  }
  SkipList(int height)
  {
    this.height = height;
    head = new Node<T>(this.height);
    this.size = 0;
  }
  public int size()
  {
    return this.size;
  }
  public int height()
  {
    return this.height;
  }
  public Node<T> head()
  {
    return this.head;
  }
  public void insert(T data)
  {
    Node<T> place = head;
    int newHeight = generateRandomHeight(height);
    HashMap<Integer, Node<T>> newBridges = new HashMap<Integer, Node<T>>();
    //using a hashmap to hold all the nodes that will point to the new node

    int i = this.height-1;
    while(i >= 0)
    {
      if(place.next(i) == null)//dropping a level if theres nothing further ahead
      {
        newBridges.put(i,place);
        i--;
        continue;
      }
      Node<T> temp = place.next(i);

      while(temp != null)//moving forward at that level until whats ahead is greater
      {
        if(data.compareTo(temp.data) > 0)
        {
          place = temp;
          temp = temp.next(i);
        }
        else
          break;
      }
      newBridges.put(i,place);
      i--;
    }
    //System.out.println(Arrays.asList(newBridges));
    Node<T> newNode = new Node<T>(data, newHeight);//initializing new node

    for(int j=0; j < newHeight; j++)
    {
      Node<T> temp = newBridges.get(j);
      if( temp.next(j) != null)
        newNode.setNext(j,temp.next(j));//setting the next levels to the nodes that wil now point to the new node
      temp.setNext(j, newNode);//point all the old nodes to the newnode
    }
    this.size++;

    if(this.height < getMaxHeight(size))//growing ehight if size of list too big
      growSkipList();
  }
  public void insert(T data, int height)
  {
    //exatly the same as the previous method except without a randomly generated height

    Node<T> place = head;
    HashMap<Integer, Node<T>> newBridges = new HashMap<Integer, Node<T>>();

    int i = this.height-1;
    while(i >= 0)
    {
      if(place.next(i) == null)
      {
        newBridges.put(i,place);
        i--;
        continue;
      }
      Node<T> temp = place.next(i);

      while(temp != null)
      {
        if(data.compareTo(temp.data) > 0)
        {
          place = temp;
          temp = temp.next(i);
        }
        else
          break;
      }
      newBridges.put(i,place);
      i--;
    }
    Node<T> newNode = new Node<T>(data, height);

    for(int j=0; j < height; j++)
    {
      Node<T> temp = newBridges.get(j);
      if( temp.next(j) != null)
        newNode.setNext(j,temp.next(j));
        temp.setNext(j, newNode);
    }
    this.size++;
    if(this.height < getMaxHeight(size))
      growSkipList();
  }

  public void delete(T data)
  {
    Node<T> place = head;
    HashMap<Integer, Node<T>> newBridges = new HashMap<Integer, Node<T>>();
    int i = this.height-1;
    while(i >= 0)
    {
      if(place.next(i) == null)
      {
        newBridges.put(i,place);
        i--;
        continue;
      }
      Node<T> temp = place.next(i);

      while(temp != null)
      {
        if(data.compareTo(temp.data) > 0)
        {
          place = temp;
          temp = temp.next(i);
        }
        else
          break;
      }
      newBridges.put(i,place);
      i--;
    }
    //identical to insert until this point
      //checks if the target node has the right data to delete
      place = place.next(0);

    if(place == null || data.compareTo(place.data) != 0)
      return;

      //bridges all the next nodes for each level that the deleted node contained
    for(int j=0; j < place.height; j++)
    {
      Node<T> temp = newBridges.get(j);
      if( temp.next(j) != null)
        temp.setNext(j,place.next(j));
    }
    this.size--;
    if(this.height > getMaxHeight(size) && this.height != 1)
      trimSkipList();
  }

  //same as insert except uses >= to check if the data is found
  // as there is no need to find left most in this case
  public boolean contains(T data)
  {
    Node<T> place = head;
    int i = this.height-1;
    while(i >= 0)
    {
      if(place.next(i) == null)
      {
        i--;
        continue;
      }
      Node<T> temp = place.next(i);

      while(temp != null)
      {
        if(data.compareTo(temp.data) >= 0)
        {
          if(data.compareTo(temp.data) == 0)
            return true;
          place = temp;
          temp = temp.next(i);
        }
        else
          break;
      }
      i--;
    }

    return false;
  }

  //lops through finds leftmost node where data would be contained
  //chekcs if node contains that data if not returns null
  public Node<T> get(T data)
  {
    Node<T> place = head;
    int i = this.height-1;
    while(i >= 0)
    {
      if(place.next(i) == null)
      {
        i--;
        continue;
      }
      Node<T> temp = place.next(i);

      while(temp != null)
      {
        if(data.compareTo(temp.data) > 0)
        {
          place = temp;
          temp = temp.next(i);
        }
        else
          break;
      }
      i--;
    }
    if(place.next(0) != null)
      place = place.next(0);
    if(data.compareTo(place.data) == 0)
      return place;

    return null;
  }
  public static double difficultyRating()
  {
    return 3.0;
  }
  public static double hoursSpent()
  {
    return 7.1;
  }
  private static int getMaxHeight(int n)
  {
    return (int)(Math.log(n)/Math.log(2) + 0.99999);
  }

  // flips a coin for each level then moves onto next one if not heads
  //if none are chosen loops through coin flipping again
  // since the sum of all the possibilities approaches but does not reach 1
  private static int generateRandomHeight(int maxHeight)
  {
    int h = 1;
    while(h <= maxHeight)
    {
      Random rand = new Random();
      int  n = rand.nextInt(2);
      if(n==1)
      {
        return h;
      }
      if(h++ == maxHeight)
        h=1;
    }
    return h;
  }
  //grows the skip list by probalistically choosing half of the highest nodes
  // increasing their height and linking them at the highest level
  private void growSkipList()
  {
    this.height++;
    this.head.height++;
    this.head.Nexts.add(null);
    Node<T> place = head;
    Node<T> previous = head;

    while(place.next(this.height-2) != null)
    {
      place = place.next(this.height-2);
      place.maybeGrow();
      if(place.height == this.height)
        {
          previous.setNext(this.height-1, place);
          previous = place;
        }
    }

  }

  //trimps the skiplist down to the appropriate level
  // by looping through that level and changing heights
  private void trimSkipList()
  {

    this.height = getMaxHeight(this.size);


    Node<T> place = head;
    while(place != null)
    {
      Node<T> next = place.next(this.height);
      if(place.height > this.height)
        place.height = this.height;
      place = next;
    }

  }


}
