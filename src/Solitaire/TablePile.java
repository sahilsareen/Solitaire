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
 * A subclass of Pile which models the tableau piles 
 */

import java.awt.*;
import javax.swing.JComponent;

public class TablePile extends Pile{
    protected final static int tableCardShift = 15;
    // The number of cards face up on this tableau pile
    protected int cardsShowing;
    
    // Initialised with initSize cards
    public TablePile( int x, int y, int initSize ) {
	super( x, y );
	for ( int i = 0 ; i < initSize ; ++i ) {
	    push( Solitaire.getTalon().pop() );
	}
	if( initSize > 0 ) {
	    topCard().turnOver();
	    cardsShowing = 1;
	}
    }
    
    public boolean accepts( Card someCard ) {
	if ( isEmpty() ) {
	    // Only Kings can be put on empty spaces
	    return someCard.getValue() == 13;
	} else {
	    // The colour of topCard must be opposite to that of someCard
	    // The value of topCard must be one more than that of someCard
	    Card    topCard       = topCard();
	    Colour  topCardColour = topCard.getColour();
	    boolean correctColour;
	    if (topCardColour.equals( Colour.Red ) ) {
            correctColour = someCard.getColour().equals( Colour.Black );
	    } else if (topCardColour.equals( Colour.Black ) ) {
            correctColour = someCard.getColour().equals( Colour.Red );
	    } else {
            throw new IllegalArgumentException( "Bad Card Colour" );
	    }
	    return correctColour && topCard.getValue() == someCard.getValue() + 1;
	}
    }
    
    public void addShownCards( int more ) {
	cardsShowing += more;
    }
    
    public void removeShownCards( int fewer ) {
	cardsShowing -= fewer;
    }
    
    public int checkShownCards() {
	return cardsShowing;
    }
    
    public TablePile pickUp( int numberNeeded ) {
	if ( cardsShowing == 0 ) {
	    return null;
	}
	TablePile p = super.pickUp( numberNeeded );
	if ( p != null ) {
        removeShownCards( numberNeeded );
	}
	return p;
    }
    
    public void putDown( Pile source ) {
	if ( source != null && !source.isEmpty() ) {
	    source.reversePile();
	    int i = source.getSize();
	    while( i > 0 ) {
		    push( source.pop() );
            --i;
            addShownCards( 1 );
	    }
	}
    }
    
    // Accounts for the shift from all the cards in the pile
    public boolean containsPoint( int xpt, int ypt ) {
	return ( ( x <= xpt ) && ( xpt <= x + Card.width ) &&
             ( y <= ypt ) && ( ypt <= y + Card.height + ( tableCardShift * ( size - 1 ) ) ) );
    }
    
    public void show( Graphics g, JComponent c ) {
    	if ( isEmpty() ) {
	        Card.getCardOutline().show( g, c, x, y );
    	} else {
	        int currentY = y;
	        for ( Card someCard : cards ) {
		        if ( someCard.isFaceUp() ) {
                   someCard.show( g, c, x, currentY );
	 	        } else {
                    Card.getCardBack().show( g, c, x, currentY );
		        }
                currentY+=tableCardShift;
            }
    	}
    }
}
