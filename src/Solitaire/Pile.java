/* -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * Copyright (C) 2016 Sahil Sareen
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version. See http://www.gnu.org/copyleft/gpl.html the full text of the
 * license.
 */

package Solitaire;

/**
 * An abstract class that models a pile
 * Each of the four different types of piles are subclasses of this class
 */

import java.awt.*;
import java.util.Stack;
import java.util.EmptyStackException;
import javax.swing.JComponent;

public abstract class Pile{
    // Coordinates of the upper left corner of the pile
    protected int x, y;
    protected Stack< Card > cards;
    protected int size;
    
    
    public String toString(){
	String s = "[";
	Object[] l = cards.toArray();
	for ( int i = 0 ; i < l.length-1 ; ++i ) {
	    s += l[ i ].toString(); 
	    s += ", ";
	}
	s += l[ l.length-1 ].toString() + "]";
	return s;
    }
    
    public Pile( int x, int y ) {
	this.x = x;
	this.y = y;
	cards = new Stack< Card >();
	size = 0;
    }
	
    public void push( Card someCard ) {
	    cards.push( someCard );
	    ++size;
    }
    
    public final Card pop() {
	try {
	    --size;
	    return cards.pop();
	} catch (EmptyStackException ese) {
	    size = 0;
	    return null;
	}
    }
    
    // Picks up numberNeeded cards from the pile
    // and returns them a TablePile
    public TablePile pickUp(int numberNeeded) {
	if ( !isEmpty() ) {
	    assert( numberNeeded <= size );
	    Pile rev = new TablePile ( x, y, 0 );
	    TablePile p = new TablePile ( x, y, 0 );
	    int i = numberNeeded;
	    while( i > 0 ) {
		    rev.push( pop() );
		    --i;
	    }
	    while( i < numberNeeded ) {
		    p.push( rev.pop() );
		    ++i;
	    }
	    return p;
	} else {
	    return null;
	}
    }
    
    //puts source onto this pile
    public void putDown( Pile source ) {
	if ( source != null && !source.isEmpty() ) {
	    source.reversePile();
	    int i = source.getSize();
	    while( i > 0 ) {
		    push( source.pop() );
		    --i;
	    }
	}
    }
    
    @SuppressWarnings("unchecked")
    public void reversePile() {
	Stack< Card > temp = new Stack< Card >();
	while( !cards.empty() ) {
	    temp.push( cards.pop() );
	}
	size = temp.size();
	cards = ( Stack< Card > ) temp.clone();
	temp = null;
    }

    
    public void reposition(int newX, int newY) {
	x = newX;
	y = newY;
    }
	
    public Stack< Card > getCards() {
	return cards;
    }
    
    public int getX() {
	return x;
    }
	
    public int getY() {
	return y;
    }
	
    public int getSize() {
	return size;
    }
	
    public void clickedAt( int xpt, int ypt ) {
	//performs an action when the mouse is clicked at point (xpt, ypt)
	//does nothing in this class
	//overridden in subclasses
    }
	
    public final Card topCard() {
	if( !cards.isEmpty() )
	    return cards.peek();
	return null;
    }
	
    public final Card bottomCard() {
	if( isEmpty() ) {
	    return null;
	}
	return cards.get(0);
    }
	
    public boolean accepts( Card someCard ) {
	return false;
    }
    
    public boolean containsPoint( int xpt, int ypt ) {
	return ( ( xpt >= x ) && ( xpt <= x + Card.width )
		 && ( ypt >= y ) && ( ypt <= y + Card.height ) );
    }
	
    public final boolean isEmpty() {
	return cards.isEmpty();
    }
	
    public void show( Graphics g, JComponent c ) {
    	if( isEmpty() ) {
    		Card.getCardOutline().show( g, c, x, y );
    	} else {
	    if(topCard().isFaceUp() ) {
		    topCard().show( g, c, x, y );
	    } else {
		    Card.getCardBack().show( g, c, x, y );
	    }
    	}
    }
}
