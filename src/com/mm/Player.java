/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mm;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author MAHBUB
 */
public class Player {

    String name;
    Card card[];
    Card gotByPass[];

    List<Card> club;
    List<Card> dice;
    List<Card> spade;
    List<Card> heart;

    public int numOfClub, numOfDice, numOfSpade, numOfHeart;

    int score;

    public Player(String playerName)
    {
        card = new Card[13];
        gotByPass = new Card[3];
        name = playerName;

        club = new LinkedList<Card>();
        dice = new LinkedList<Card>();
        spade = new LinkedList<Card>();
        heart = new LinkedList<Card>();
        numOfClub = numOfDice = numOfSpade = numOfHeart = 0;
    }

    public void setCards(Card c[])                      // Set 13 cards to a player.
    {
        int i;
        for(i=0; i<13; i++) {
            card[i] = new Card();
            card[i] = c[i];
        }
        SeperateCard();
        SortAsHearts();
    }

    public void GiveCard(Card c[])                      // Pass 3 cards
    {
        for(int i=0; i<13; i++)
            if(card[i].isEqual(c[0]) || card[i].isEqual(c[1]) || card[i].isEqual(c[2]))
                card[i] = null;
    }

    public Card[] GiveCardAI()                          //
    {
        int i, j, k;
        List< Card > tempList = new LinkedList< Card >();
        Card ret[] = new Card[3];
        
        for(i=0; i<13; i++)
            tempList.add(card[i]);

        Collections.sort(tempList, new CardComparatorDecreasing());

        ret[0] = tempList.get(0);
        ret[1] = tempList.get(1);
        ret[2] = tempList.get(2);

        for(i=0; i<13; i++)
            if(card[i].isEqual(ret[0]) || card[i].isEqual(ret[1]) || card[i].isEqual(ret[2]))
                card[i] = null;

        return ret;
    }

    public void takeCard(Card c[])                      // Takes 3 cards passed by other player.
    {
        gotByPass[0] = c[0];
        gotByPass[1] = c[1];
        gotByPass[2] = c[2];
    }

    public void combineCard()                           // Combine taken cards and existing cards.
    {
        int i, j;
        for(i=0, j=0; i<13; i++)
            if(card[i] == null)
                card[i] = gotByPass[j++];
        SeperateCard();
        SortAsHearts();
    }

    private void SeperateCard()                         // Seperate all cards according suit.
    {
        int i;

        club.clear();
        dice.clear();
        spade.clear();
        heart.clear();
//        System.out.println(club.size());
        for(i=0; i<13; i++) {
            if(card[i].suit == 'C')
                club.add(card[i]);
            else if(card[i].suit == 'D')
                dice.add(card[i]);
            else if(card[i].suit == 'S')
                spade.add(card[i]);
            else if(card[i].suit == 'H')
                heart.add(card[i]);
        }

        Collections.sort(club, new CardComparatorIncreasing());
        Collections.sort(dice, new CardComparatorIncreasing());
        Collections.sort(spade, new CardComparatorIncreasing());
        Collections.sort(heart, new CardComparatorIncreasing());
    }

    public void SortAsHearts()                      // First club, then Dice, Spade, Heart
    {
        int i, j, ptr;
        Card tempCard = new Card();

        ptr = 0;
        for(i=0; i<club.size(); i++) {
            card[ptr++] = club.get(i);
        }
        for(i=0; i<dice.size(); i++) {
            card[ptr++] = dice.get(i);
        }
        for(i=0; i<spade.size(); i++) {
            card[ptr++] = spade.get(i);
        }
        for(i=0; i<heart.size(); i++) {
            card[ptr++] = heart.get(i);
        }
    }

    public Card moveCard2C()                            // Move 2 of Clubs
    {
        int i;
        Card twoOfClub = new Card("2C");

        for(i=0; i<13; i++)
            if(card[i].isEqual(twoOfClub)) {
                card[i] = null;
                return twoOfClub;
            }
        return null;
    }

    public boolean hasCard(Card c)                          //
    {
        for(int i=0; i<13; i++)
            if(card[i].toString().equals(c.toString()))
                return true;
        return false;
    }

    public Card moveCardAI()                                // Called when moves as first player
    {
        Card tempCard = new Card();

        for(int i=0; i<13; i++)
            if(card[i] != null) {
                tempCard = card[i];
                card[i] = null;
                break;
            }
        return tempCard;
    }

    public Card moveCardAI(Card other[])                    // 
    {
        int i, j;
        Card tempCard = null;

        for(i=0; i<13; i++)
            if(card[i] != null && other[0].suit == card[i].suit) {
                tempCard = card[i];
                card[i] = null;
                break;
            }

        if(tempCard != null)
            return tempCard;
        else {
            for(i=0; i<13; i++)
                if(card[i] != null) {
                    tempCard = card[i];
                    card[i] = null;
                    return tempCard;
                }
        }
        return tempCard;
    }

    public Card moveCardAI(Vector<Card> other)              // 
    {
        int i, j;
        Card tempCard = null;

        for(i=0; i<13; i++)
            if(card[i] != null && other.get(0).suit == card[i].suit) {
                tempCard = card[i];
                card[i] = null;
                break;
            }

        if(tempCard != null)
            return tempCard;
        else {
            for(i=0; i<13; i++)
                if(card[i] != null) {
                    tempCard = card[i];
                    card[i] = null;
                    return tempCard;
                }
        }
        return tempCard;
    }


    public void printPlayer()
    {
        System.out.println("Player Name : " + name);

        int i;
        for(i=0; i<13; i++)
            System.out.print( card[i].toString() + " ");
        System.out.println("\n");
    }

    public void printCardCDSH()
    {
        System.out.println("Player Name : " + name);
        Iterator<Card> It = club.iterator();
        while(It.hasNext()) {
            System.out.print(It.next().toString() + " ");
        }
        System.out.println();

        It = dice.iterator();
        while(It.hasNext()) {
            System.out.print(It.next().toString() + " ");
        }
        System.out.println();

        It = spade.iterator();
        while(It.hasNext()) {
            System.out.print(It.next().toString() + " ");
        }
        System.out.println();

        It = heart.iterator();
        while(It.hasNext()) {
            System.out.print(It.next().toString() + " ");
        }
        System.out.println();

    }

    public void printGotByPassCard()
    {
        int i;
        for(i=0; i<3; i++)
            System.out.print( gotByPass[i].toString() + " ");
        System.out.println();
    }

}
