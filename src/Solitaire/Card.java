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
 * A class for a single playing card 
 */

import java.awt.*;

import javax.swing.JComponent;

public class Card {
    ////////////////////////////////////////////////////////////////////////////////////////
    // --- private members ---
    ////////////////////////////////////////////////////////////////////////////////////////

    private Image im;  // its picture
    private int value;
    private String suit;
    private boolean faceUp; // is its face shown?
    private Colour colour;

    // the directory where the card images are to be found
    private final static String directory = "cards",
        extension = ".gif",
        fpBaseFileName = "fpBase0";
    private final static Image fpShade = Toolkit.getDefaultToolkit().getImage( "cards/shade.gif" );
    private final static String[] backs = new String[] {
        "back001", "back101", "back102", "back111", "back121",
        "back131", "back132", "back191", "back192"
    };
    private final static String[] outlines = new String[] {
        "bottom01", "bottom02", "bottom03"
    };

    ////////////////////////////////////////////////////////////////////////////////////////
    // --- public members ---
    ////////////////////////////////////////////////////////////////////////////////////////

    public final static int width = 73;
    public final static int height = 97;
    public static int backIndex = 0;
    public static int outlineIndex = 0;
    public static String cardBackFilename = backs[ backIndex ],
        cardOutlineFilename = outlines[ outlineIndex ];

    ////////////////////////////////////////////////////////////////////////////////////////
    // -- member functions ---
    ////////////////////////////////////////////////////////////////////////////////////////

    // Grab a standard card.  s is the suit; card is the card number in the range 1-13 
    public static Card getCard( Suit s, int card ) {
        return new Card( directory + CardFile( s, card ) );
    }
    
    // Grab a card back
    public static Card getCardBack() {
        return new Card( directory + "/" + cardBackFilename + extension );
    }

    // Grab a card outline
    public static Card getCardOutline() {
        return new Card( directory + "/" + cardOutlineFilename + extension );
    }

    // outline for foundation pile bases
    public static Card getFoundationBase( int i ) {
        return new Card( directory + "/" + fpBaseFileName + i + extension );
    }

    private Card( String name ) {
	try {
	    im = Toolkit.getDefaultToolkit().getImage( name );
	    String[] nameArray = name.split( "/" );
        
	    // Gives the last element of the array ("(n)(s)(extension)")
	    String last = nameArray[ nameArray.length - 1 ];
	    String n = last.substring( 0, 2 );

	    if ( n.equals( "ba" ) ) {
            value  = 14;
            suit   = "x";
            colour = Colour.Neither;
            faceUp = false;
	    } else if ( n.equals( "bo" ) ) {
            value  = 0;
            suit   = "x";
            colour = Colour.Neither;
            faceUp = false;
	    } else if ( n.equals( "fp" ) ){
            value  = 0;
            faceUp = false;
            n      = last.substring(7, 8);
            switch ( n ) {
            case "1":	
                suit   = "s";
                colour = Colour.Black;
                break;
            case "2":
                suit   = "h";
                colour = Colour.Red;
                break;
            case "3":
                suit   = "c";
                colour = Colour.Black;
                break;
            case "4":
                suit   = "d";
                colour = Colour.Red;
                break;
                
            }
	    } else {
            value = Integer.valueOf( n );
            suit  = last.substring( 2, 3 );
            if ( suit.equals( "h" ) || suit.equals( "d" ) ) {
                colour = Colour.Red;
            } else {
                colour = Colour.Black;
            }
            faceUp = true;
	    }
	} catch ( Exception e ) {
	    System.err.println( "Error " + e.getMessage() );
	}
    }
    
    Card(int value, Suit suit) {
    	// This constructor will not be used for the card back or outline
    	this.value = value;
    	switch (suit){
        case Clubs:		
            this.suit = "c";
            colour    = Colour.Black;
            break;
        case Diamonds:	
            this.suit = "d";
            colour    = Colour.Red;
            break;
        case Hearts:
            this.suit = "h";
            colour    = Colour.Red;
            break;
        case Spades:
            this.suit = "s";
            colour    = Colour.Black;
            break;    		
    	}
    	faceUp = false;
    	try {
            im = Toolkit.getDefaultToolkit().getImage( directory + CardFile( suit, value ) );
    	} catch ( Exception e ) {
    	    System.err.println( "Error " + e.getMessage() );
    	}
    }
    
    // Draw the picture of a card onto a graphics context g, positioned at (x,y)
    public void show( Graphics g, JComponent c, int x, int y ) {
    	if ( value == 0 ) {
            g.drawImage( fpShade, x, y, c );
    	}
    	g.drawImage( im, x, y, c );
    }
    
    public boolean isFaceUp() {
    	return faceUp;
    }
    
    public void showFace() {
    	faceUp = true;
    }
    
    public void hideFace() {
    	faceUp = false;
    }
    
    public void turnOver() {
    	faceUp = !faceUp;
    }
    
    public int getValue() {
    	return value;
    }
    
    public String getSuit() {
    	return suit;
    }
    
    public Colour getColour() {
    	return colour;
    }
    
    
    // Change the cardBackFilename
    public static void incBack() {
    	backIndex        = ( backIndex + 1 ) % backs.length;
    	cardBackFilename = backs[ backIndex ];
    }
    
    public static void decBack() {
    	if ( backIndex == 0 ) {
            backIndex = backs.length - 1;
    	} else {
            backIndex--;
    	}
    	cardBackFilename = backs[ backIndex ];
    }

    // Change the cardOutlineFilename
    public static void incOutline() {
    	outlineIndex        = ( outlineIndex + 1 ) % outlines.length;
    	cardOutlineFilename = outlines[ outlineIndex ];
    }
    
    public static void decOutline() {
    	if ( outlineIndex == 0 ) {
            outlineIndex = outlines.length-1;
    	} else {
            outlineIndex--;
    	}
    	cardOutlineFilename = outlines[outlineIndex];
    }
    
    public String toString(){
    	return "(" + value + " of " + suit + ", " + isFaceUp() + ")";
    }

    protected static String CardFile( Suit s, int card ) throws IllegalArgumentException {

        char sc;	
        if ( card < 1 || card > Solitaire.getSuitsize() )
            throw new IllegalArgumentException( "Bad Card Number" );
        
        if ( s == Suit.Clubs )
            sc = 'c';
        else if ( s==Suit.Diamonds )
            sc = 'd';
        else if ( s==Suit.Hearts )
	    sc = 'h';
        else if ( s==Suit.Spades )
            sc = 's';
        else
            throw new IllegalArgumentException( "Bad Card Suit" );
        
        if ( card < 10 )
            return "/0" + card + sc + extension;
        else
            return "/" + card + sc + extension;
    }
}
