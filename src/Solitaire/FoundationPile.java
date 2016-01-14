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
 * A subclass of Pile which models the foundations 
 */

import java.awt.Graphics;
import javax.swing.JComponent;

class FoundationPile extends Pile {
    private int suit;
    
    public FoundationPile( int x, int y, int i ) {
	super( x, y );
	suit = i;
    }
    
    public boolean accepts( Card someCard ) {
	if ( isEmpty() ) {
	    return someCard.getValue() == 1 && someCard.getSuit() == intToSuit( suit );
	}
	Card topCard = topCard();
	return ( ( someCard.getSuit() == intToSuit( suit ) )
		 && someCard.getValue() == 1 + topCard.getValue() );
    }
    
    // Foundation piles only show the top card
    public void show( Graphics g, JComponent c ){
    	if ( isEmpty() ) {
	    Card.getFoundationBase( suit ).show( g, c, x, y );
    	} else {
	    topCard().show( g, c, x, y );
    	}
    }
    
    private String intToSuit(int i){
    	switch (i) {
    	case 1: return "s";
    	case 2: return "h";
    	case 3: return "c";
    	case 4: return "d";
    	default: throw new IndexOutOfBoundsException();
    	}
    }
    
    private Suit intToSuit2(int i){
    	switch (i) {
    	case 1: return Suit.Spades;
    	case 2: return Suit.Hearts;
    	case 3: return Suit.Clubs;
    	case 4: return Suit.Diamonds;
    	default: throw new IndexOutOfBoundsException();
    	}
    }
}
