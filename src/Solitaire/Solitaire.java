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

/**
 * The main class for the Solitaire application
 * Uses the Singleton pattern and initialises all piles and the window
 */

package Solitaire;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.sun.awt.AWTUtilities;

@SuppressWarnings("serial")
public class Solitaire extends JApplet {	
    private static final int suitSize = 13;
    private static final int baseX    = 40,
        baseY  = 10,
        shiftX = 80,
        shiftY = 15;
    public static final int TableWidth = 640, 
        TableHeight = 500;
    static GameStates currentState = GameStates.BAD_STATE;
    
    // Default table window size
    static protected JFrame outerFrame; // The whole window with its frame
    static protected Solitaire innerFrame; // The inside of the frame
    static protected JPanel buttonPanel;
    static protected CardTable table;
    
    private static Talon talon;
    private static WastePile wastePile;
    private static TablePile[] tablePile;
    private static FoundationPile[] foundationPile;

    private static Solitaire instance;

    private Solitaire() {
    	initialisePiles();
    }

    public static Solitaire instance() {
    	if (instance == null){
            instance = new Solitaire();
    	}
    	return instance;
    }

    // Check if the player has won the game
    public static void isGameOver() {
	boolean won = true;
	for (int i=0; i<4; i++){
	    if (Solitaire.getFoundationPile()[i].getSize()!=13){
		won = false;
	    }
	}
	if (won) {
	    // Show a popup if the player has won and change the state
	    currentState = GameStates.GAME_OVER;
	    displayGameOverDialog();
	}
    }
    
    public static void displayGameOverDialog() {
	JOptionPane.showMessageDialog(table,
			"You win! Well done",
			"Congratulations",
			JOptionPane.PLAIN_MESSAGE);
    }
    
    // TablePile and foundationPile are arrays that hold all of their respective piles
    public static void initialisePiles() {
   	currentState = GameStates.INITIALISING;
	tablePile = new TablePile[ 7 ];
	foundationPile = new FoundationPile[ 4 ];
	talon = new Talon( CardTable.Margin, CardTable.Margin );
	wastePile      = new WastePile( CardTable.Margin + CardTable.XShift, CardTable.Margin );
	
	for ( int i = 0 ; i < 4 ; ++i ) {
	    foundationPile[ i ] = new FoundationPile( CardTable.Margin +( 3 + i ) * CardTable.XShift,
	        CardTable.Margin, 
	        i + 1 );
	}
	for ( int i = 0 ; i < 7 ; ++i ) {
	    tablePile[ i ] = new TablePile( CardTable.Margin + ( i ) * CardTable.XShift,
	        CardTable.Margin+CardTable.YShift,
	        i + 1 );
	}
	currentState = GameStates.GAME_IN_PROGRESS;
    }
    
	public static void main(String[] args) {
	    outerFrame = new JFrame( "Solitaire");
	    innerFrame = new Solitaire();
	    buttonPanel = new JPanel(); 
	    buttonPanel.setLayout( new GridLayout( 4, 2 ) );
	    innerFrame.setLayout( new FlowLayout() );
	    outerFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    
	    JMenuBar mb = new JMenuBar();
	    outerFrame.setJMenuBar( mb );
	    
	    JMenu SolitaireMenu = new JMenu( "Solitaire" );
	    mb.add( SolitaireMenu );
	    
	    JMenuItem newGame  = new JMenuItem( "New Game" );
	    JMenuItem quitGame = new JMenuItem( "Quit Game" );
	    
	    newGame.addActionListener( new ActionListener() {
		    @Override
			public void actionPerformed( ActionEvent e ) {
			initialisePiles();
			table.repaint();
		    } } );
	    
	    quitGame.addActionListener( new ActionListener() {
		    @Override
			public void actionPerformed( ActionEvent e ) {
			System.exit( 0 );
		    } 
		} );
	    SolitaireMenu.add( newGame );
	    SolitaireMenu.add( quitGame );
	    
	    // Create a button to change the back
	    JButton incBackIndex = new JButton("Card back -->");
	    incBackIndex.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			Card.incBack();
			table.repaint();
		    }
		});
	    // And add the button to the panel
	    buttonPanel.add( incBackIndex );
	    
	    // Create a button to return to the previous back
	    JButton decBackIndex = new JButton("Card back <--");
	    decBackIndex.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			Card.decBack();
			table.repaint();
		    }
		} );
	    // And add the button to the panel
	    buttonPanel.add( decBackIndex );
	    
	    // Create a button to change the outline
	    JButton incOutlineIndex = new JButton("Card outline -->");
	    incOutlineIndex.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			Card.incOutline();
			table.repaint();
		    }
		} );
	    // And add the button to the panel
	    buttonPanel.add( incOutlineIndex );
	    
	    // Create a button to return to the previous outline
	    JButton decOutlineIndex = new JButton("Card outline <--");
	    decOutlineIndex.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			Card.decOutline();
			table.repaint();
		    }
		} );
	    // And add the button to the panel
	    buttonPanel.add(decOutlineIndex);
	    
	    // Create a button the shuffle the talon
	    JButton bs = new JButton( "Shuffle" );
	    bs.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			// Shuffle the cards
			Talon.Shuffle( talon.getCards() );
			table.clearMarkers();
			table.repaint();
		    }
		});
	    // And add the button to the panel
	    buttonPanel.add( bs );
	    
	    // Create a quit button
	    JButton bq = new JButton( "Quit" );
	    bq.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			System.exit(0);
		    }
		} );
	    buttonPanel.add( bq );
	    
	    // Create a button to reset the board
	    JButton bng = new JButton( "New Game" );
	    bng.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			currentState = GameStates.INITIALISING;
			initialisePiles();
			currentState = GameStates.GAME_IN_PROGRESS;
			table.repaint();
		    }
		});
	    
	    // And add the button to the panel
	    buttonPanel.add(bng);
	    
	    table = new CardTable();		
	    table.setPreferredSize( new Dimension( TableWidth, TableHeight ) );
	    innerFrame.add( table );
	    innerFrame.add( buttonPanel );
	    
	    outerFrame.add( innerFrame );
	    outerFrame.pack();
	    outerFrame.setVisible( true );
	}
    
    // Getters and setters
    public static int getSuitsize() {
	return suitSize;
    }
    
    public static int getBasex() {
	return baseX;
    }
    
    public static int getBasey() {
	return baseY;
    }
    
    public static int getShiftx() {
	return shiftX;
    }
    
    public static int getShifty() {
	return shiftY;
    }
    
    public static Talon getTalon() {
	return talon;
    }
    
    public static WastePile getWastePile() {
	return wastePile;
    }
    
    public static TablePile[] getTablePile() {
	return tablePile;
    }
    
    public static FoundationPile[] getFoundationPile() {
	return foundationPile;
    }
    
    public static void setTalon(Talon talon) {
	Solitaire.talon = talon;
    }
    
    public static void setWastePile(WastePile wastePile) {
	Solitaire.wastePile = wastePile;
    }
    
    public static void setTablePile(TablePile[] tablePile) {
	Solitaire.tablePile = tablePile;
    }
    
    public static void setFoundationPile(FoundationPile[] foundationPile) {
	Solitaire.foundationPile = foundationPile;
    }
}
