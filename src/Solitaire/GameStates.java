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

/*
 * An enumerated type to distinguish between the 3 states that game can have
 * in a way that can be type-checked 
 */

public enum GameStates {
    INITIALISING,
    GAME_IN_PROGRESS,
    GAME_OVER,
    BAD_STATE
}
