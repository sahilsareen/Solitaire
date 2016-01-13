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
 * A subclass of Pile which models the waste pile 
 */

import java.awt.Graphics;
import javax.swing.JComponent;

public class WastePile extends Pile {
    protected final static int wasteCardShift = 15;
    
    public WastePile( int x, int y ) {
	super( x, y );
    }
    
    public void clickedAt( int x, int y ) {
    	if ( isEmpty() ) {
	        return;
    	}
    }
    
    public void show( Graphics g, JComponent c ) {
    	if ( isEmpty() ) {
	        Card.getCardOutline().show( g, c, x, y );
    	} else {
	        Card c1 = pop();
	        Card c2 = c1;
	        Card c3 = c1;
	        if ( !isEmpty() ) {
		        c2 = pop();
	        }
	        if ( !isEmpty() ) {
		        c3 = pop();
	        }
	        // Each card in a pack is unique so
	        // c3 will only be equal to c1 if
	        // c3 was not popped off on line 34
	        if ( c3 != c1 ) {
	        	push( c3 );
	        	c3.show( g, c, x, y );
	        }
	        if ( c2 != c1 ) {
	        	push(c2);
	        	c2.show( g, c, x + wasteCardShift, y );
	        }
	        push( c1 );
	        c1.show( g, c, x + 2 * wasteCardShift, y );
    		}
    }
}

