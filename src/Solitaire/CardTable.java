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
 * A subclass of JPanel that incorporates the visual idea of a card table, and piles of cards upon it 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;


@SuppressWarnings("serial")
class CardTable extends JPanel {
    protected static final int Margin = 40,
	XShift = 80,
	YShift = 160,
	CardShift = 15;
    static int anchorX, anchorY, currentX, currentY, dX, dY;
    protected TablePile dragging = null;
    protected Pile draggedFrom = null;

    public int pickedFromPileIdx = -1;
    public enum PILE_TYPE {
    	FOUNDATION_PILE,
        TABLE_PILE,
        WASTE_PILE,
        TALON,
        BAD_PILE
    };
    public PILE_TYPE pickedFromPileType = PILE_TYPE.BAD_PILE;

    public void clearMarkers() {
    	dragging = null;
        draggedFrom = null;
        pickedFromPileIdx = -1;
        pickedFromPileType = PILE_TYPE.BAD_PILE;
    }

    public void paintComponent( Graphics g ) {
	super.paintComponent( g );
    	g.setColor( Color.green );
    	g.fillRect( 0, 0, getWidth(), getHeight() );

    	// Show the tablePiles
    	for( int i = 0 ; i < 7 ; ++i ) {
            Solitaire.getTablePile()[ i ].show( g, this );
    	}

    	// Show the foundationPiles
    	for( int i = 0 ; i < 4 ; ++i ) {
            Solitaire.getFoundationPile()[ i ].show( g, this );
    	}

    	// Show the talon and wastePile
    	Solitaire.getTalon().show( g, this );
    	Solitaire.getWastePile().show( g, this );

    	// Show the card the player is playing
    	if ( dragging != null ){
	    dragging.show( g, this );
    	}
    }

    public CardTable() {
    	// Define, instantiate and register a MouseListener object
    	addMouseListener( new MouseAdapter() {
            public void mouseClicked( MouseEvent e ) {
                if (Solitaire.currentState == GameStates.GAME_OVER) {
                    return;
                }
                anchorX = e.getX();
                anchorY = e.getY();
                if ( Solitaire.getTalon().containsPoint( anchorX, anchorY ) ) {
                    Solitaire.getTalon().clickedAt( anchorX, anchorY );
                } else {
                    for ( int i = 0 ; i < 7 ; ++i ){
                        if ( Solitaire.getTablePile()[ i ].containsPoint( anchorX, anchorY ) ) {
                            TablePile tp = Solitaire.getTablePile()[ i ];
                            if ( ( tp.getSize() - 1 ) * TablePile.tableCardShift + tp.getY() < anchorY &&
                                 tp.topCard().isFaceUp() == false ) {
                                tp.topCard().turnOver();
                                tp.addShownCards( 1 );
                            }
                        }
                    }
                }
            }
            });
    	
        // Define, instantiate and register a MouseListener object
    	addMouseListener( new MouseAdapter() {
		public void mousePressed( MouseEvent e ) {
		    if (Solitaire.currentState==GameStates.GAME_OVER) {
                return;
		    }
		    System.out.println("PickingFrom called");

		    // Remember the place the mouse was pressed
		    anchorX = e.getX();
		    anchorY = e.getY();
		    
		    // Check if mouse is pressed on a tablePile
		    for ( int i = 0 ; i < 7 ; ++i ) {
    			if ( Solitaire.getTablePile()[ i ].containsPoint( e.getX(), e.getY() ) ) {
                    TablePile tp = Solitaire.getTablePile()[ i ];
                    int shift = anchorY - tp.getY();
                    int cardsIgnored = shift / TablePile.tableCardShift;
                    if (cardsIgnored >= tp.getSize() ) {
                        cardsIgnored = tp.getSize() - 1;
                    }
                    int cardsSelected = tp.getSize() - cardsIgnored;
                    if ( cardsSelected <= tp.checkShownCards() ) {
                        System.out.println( cardsIgnored + " cards will be left in pile#-" + i );
                        System.out.println( cardsSelected + " cards will be picked up from pile#-" + i );
                        dragging = tp.pickUp( cardsSelected );
                    }

                    if ( dragging != null ) {
                        System.out.println("I picked up"+dragging.toString());
                        // Set pickedFromPileIdx
                        pickedFromPileIdx = i; 
                        pickedFromPileType = PILE_TYPE.TABLE_PILE;
                        draggedFrom = Solitaire.getTablePile()[ i ];
                        dX = anchorX - dragging.getX();
                        dY = anchorY - dragging.getY() - cardsIgnored * TablePile.tableCardShift;
                    } else {
                        pickedFromPileIdx  = -1;
                    }
                    break;
    			}
		    }

		    // Check if mouse is pressed on a foundationPile
		    for ( int i = 0 ; i < 4 ; i++ ) {
    			if ( Solitaire.getFoundationPile()[ i ].containsPoint( e.getX(), e.getY() ) ) {
			    dragging = Solitaire.getFoundationPile()[ i ].pickUp( 1 );
			    dX = anchorX - dragging.getX();
			    dY = anchorY - dragging.getY();
			    pickedFromPileIdx = i;
			    pickedFromPileType = PILE_TYPE.FOUNDATION_PILE;
    			}
		    }
		    
		    // Check if mouse is pressed on the top card of the wastePile
		    if ( Solitaire.getWastePile().containsPoint( e.getX() - 2 * WastePile.wasteCardShift, e.getY() )
			 && !Solitaire.getWastePile().isEmpty() ) {
			dragging = Solitaire.getWastePile().pickUp(1);
			dX = e.getX() - dragging.getX() - 2 * WastePile.wasteCardShift;
			dY = e.getY() - dragging.getY();
			pickedFromPileIdx  = 0;
			pickedFromPileType = PILE_TYPE.WASTE_PILE;
		    }
    		}
	    });
	
    	// Define, instantiate and register a MouseListener object
    	addMouseListener(new MouseAdapter() {
		public void mouseReleased( MouseEvent e ) {
		    if (Solitaire.currentState==GameStates.GAME_OVER) {
                return;
		    }
		    System.out.println("PutDownOn called");
		    currentX = e.getX();  
		    currentY = e.getY();
		    boolean putDownCard = false;
		    if ( dragging != null && !dragging.isEmpty() ) {
			System.out.println("I'm holding"+dragging.toString());
			
			// Check if mouse is released on a tablePile
			for( int i = 0 ; i < 7 ; ++i ) {
			    if (Solitaire.getTablePile()[ i ].containsPoint( e.getX(), e.getY() ) ) {
                    System.out.println("I'm trying to put it down in pile#-"+i);
                    // This is the pile where the player is trying to putDown the card
                    // Check if this is allowed
                    if(Solitaire.getTablePile()[ i ].accepts( dragging.bottomCard() ) ) {
                        System.out.println("Pile accepted it");
                        Solitaire.getTablePile()[ i ].putDown( dragging );
                        putDownCard = true;
                    }
			    }
			}
			
			// Check if mouse is released on a foundationPile
			for( int i = 0 ; i < 4 ; ++i ) {
			    if ( Solitaire.getFoundationPile()[ i ].containsPoint( e.getX(), e.getY() )
                     && dragging.getSize() == 1 ) {
                    System.out.println("I'm trying to put it down in foundation#-"+i);
                    // This is the pile where the player is trying to putDown the card
                    // Check if this is allowed
                    if( Solitaire.getFoundationPile()[i].accepts( dragging.bottomCard() ) ) {
                        System.out.println("Pile accepted it");
                        Solitaire.getFoundationPile()[ i ].putDown( dragging );
                        putDownCard = true;
                    }
			    }
			}

			switch( pickedFromPileType ) {
			case TABLE_PILE:
			    Solitaire.getTablePile()[ pickedFromPileIdx ].putDown( dragging );
			    break;
			case FOUNDATION_PILE:
			    Solitaire.getFoundationPile()[ pickedFromPileIdx ].putDown( dragging );
			    break;
			case WASTE_PILE:
			    Solitaire.getWastePile().putDown( dragging );
			    break;
			default:
			    break;
			}
		    } else {
                System.out.println("No cards were being dragged ");			    	
		    }
		    
		    // Put the card back where it came from
		    if( !putDownCard && pickedFromPileIdx != -1 ) {
                if ( 0 < pickedFromPileIdx && pickedFromPileIdx < 7 ) {
                    Solitaire.getTablePile()[ pickedFromPileIdx ].putDown( dragging );
                    System.out.println("Put it back in pile#-"+pickedFromPileIdx);
                }
		    }
		    dragging = null;
		    repaint();
		    Solitaire.isGameOver();
		}
        }) ;
	
    	// Define, instantiate and register a MouseMotionListener object
    	addMouseMotionListener( new MouseMotionAdapter() {
		    public void mouseDragged( MouseEvent e ) {
                // draw the dragged pile as it moves around
                if (Solitaire.currentState==GameStates.GAME_OVER) {
                    return;
                }
                currentX = e.getX();
                currentY = e.getY();
                if ( dragging != null ) {
                    dragging.reposition( currentX - dX, currentY - dY );
                }
                repaint();
            }
            } ) ;
    }
    
    public Dimension getMinimumSize() {
    	return getPreferredSize();
    }
    
    public Dimension getMaximumSize() {
    	return getPreferredSize();
    }
    
}
