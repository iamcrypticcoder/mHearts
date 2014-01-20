/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mm;

import java.util.Random;

/**
 *
 * @author MAHBUB
 */
public class Deck {

    public Card card[];
    Random random;

    public Deck()
    {
        card = new Card[52];

        int value;
        char suit;

        int i;
        for(i=0, value = 0; i<52; i++, value = ( value + 1 ) % 13) {
            card[i] = new Card();
            card[i].value = value + 2;

            if(i <= 12)
                card[i].suit = 'C';
            else if(i <= 25)
                card[i].suit = 'D';
            else if(i <= 38)
                card[i].suit = 'H';
            else if(i <= 52)
                card[i].suit = 'S';
        }
    }


    public void suffle()
    {
        random = new Random();

        int i, j;
        int tempValue;
        char tempSuit;

        for(i = 0; i<52; i++) {
            j = random.nextInt(52);
        //    System.out.println(j);
            tempValue = card[i].value;
            tempSuit = card[i].suit;
            card[i].value = card[j].value;
            card[i].suit = card[j].suit;
            card[j].value = tempValue;
            card[j].suit = tempSuit;
        }

    }


    public void printDeck()
    {
        int i;
        for(i=0; i<52; i++) {
            if(i % 13 == 0)
                System.out.println();
            System.out.print(card[i].toString() + " ");
        }
        System.out.println();
    }

}
