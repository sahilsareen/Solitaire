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
 * A subclass of Pile which models the talon 
 */

import java.util.Random;
import java.util.Stack;

public class Talon extends Pile {
    
    public Talon( int x, int y ) {
	super( x, y );
	for ( Suit suit : Suit.values() ) {
	    for ( int j = 1; j <= 13 ; ++j ) {
		    push( new Card( j, suit ) );
	    }
	}
	Shuffle( cards );
    }
    
    /* 
     * Shuffle the first n cards in list in-place, using the random
     * number generator rgen
     */
    public static void Shuffle( Stack< Card > cards) {
	// Number of unshuffled cards remaining
	int n = cards.size();
	Random rgen = new Random();
	// Nothing to do when only one card remains
	while (n>1) {
	    // select a card number in the range [0..n), and swap that with position n-1
	    int cn = (int) (n * rgen.nextFloat());
	    assert cn>=0 && cn < n;
	    Card cv = cards.get( cn), lv = cards.get( n-1);
	    cards.set( cn, lv);  cards.set( n-1, cv);
	    n--;
	}
    }
    
    public void clickedAt( int x, int y ) {
    	if ( isEmpty() ) {
	    while( !Solitaire.getWastePile().isEmpty() ) {
		    Card c = Solitaire.getWastePile().pop();
		    c.hideFace();
		    push( c );
	    }
    	} else {
	    for ( int i = 0 ; i < 3 ; i++ ) {
		if ( !isEmpty() ) { 
		    // Must be checked before every pop in case fewer than 3 cards remained
		    Card c = pop();
		    c.showFace();
		    Solitaire.getWastePile().push( c );
		}
	    }
    	}
    }
}

