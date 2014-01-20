/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mm;

import java.util.Comparator;

/**
 *
 * @author MAHBUB
 */
public class Card {

    public int value;
           char suit;

    public Card()               
    {

    }

    public Card(String str)
    {
        value = charToValue(str.charAt(0));
        suit = str.charAt(1);
    }

    public static char valueToChar(int val)
    {
        char ch[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};
        return ch[val];
    }

    public static int charToValue(char symbol)
    {
        char ch[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};
        int val[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

        int i;
        for(i=0; i<15; i++)
            if(symbol == ch[i])
                return val[i];
        return 0;
    }

    public boolean isEqual(Card c)
    {
        if(this.value == c.value && this.suit == c.suit)
            return true;
        else
            return false;
    }

    public String toString()
    {
        return String.format("%c%c", valueToChar(value), suit);
    }
}

class CardComparatorIncreasing implements Comparator<Card>
{
    public int compare(Card c1, Card c2)
    {
        return c1.value - c2.value;
    }
}

class CardComparatorDecreasing implements Comparator<Card>
{
    public int compare(Card c1, Card c2)
    {
        return c2.value - c1.value;
    }
}
