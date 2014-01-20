/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainPanel.java
 *
 * Created on Jul 20, 2010, 11:11:01 PM
 */

package com.mm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
//import javax;


/**
 *
 * @author Mahbub
 */
public class MainPanel extends JPanel implements ActionListener, KeyListener {

// -------------------------- GUI Components -----------------------------------
    JLabel nameLabel[];
    JLabel label1, label2, label3;
    JLabel scoreNameLabel[];
    JLabel scoreLastLabel[];
    JLabel scoreTotalLabel[];

    CardPanel cardPanels[];
    CardPanel movedCardPanel[];

// -----------------------------------------------------------------------------

    final int INF = 999999;
// -------------------------- Game Plan Variable -------------------------------
    String playersName[] = {"", "SUPER MAN", "IRON MAN", "SPIDER MAN" };
    Deck deck;
    Player players[];    
//    Card movedCard[];

    enum PHASE {PASSPHASE, MOVEPHASE};
    enum PASS_PHASE { PASSLEFT, PASSRIGHT, PASSACCROSS, NOPASS};
    PHASE curPhase;
    PASS_PHASE curPassPhase;

    boolean isUp[] = new boolean[13];
    int totalselectedCard = 0;
    Card passCard[] = new Card[3];
    Card leadingCard = new Card();
    Vector<Card> movedCard = new Vector<Card>();

    int playerScore[] = new int[4];
    int totalScore[] = new int[4];
    int firstMover, curMover;
    int pitNumber = 1;
    boolean playerZeroMoved;

    AutoPlayerThread autoPlayerThread;
    CardRepresentationThread aniThread;
// -----------------------------------------------------------------------------
    
    MouseOperationHandler mouseHandler;

    public MainPanel() {
        initComponents();
        int i, j, k;
        
        new SplashScreen(2000);

        mouseHandler = new MouseOperationHandler();

        deck = new Deck();
        nameLabel = new JLabel[4];
        scoreNameLabel = new JLabel[4];
        scoreLastLabel = new JLabel[4];
        scoreTotalLabel = new JLabel[4];

        cardPanels = new CardPanel[52];
        movedCardPanel = new CardPanel[4];

        
        for(i=0; i<52; i++) {
            cardPanels[i] = new CardPanel();
            mainJLayeredPane.add(cardPanels[i]);

            cardPanels[i].addMouseListener(mouseHandler);
        }
        players = new Player[4];


        label1 = new JLabel("PLAYERS");
        label2 = new JLabel("POINTS");
        label3 = new JLabel("TOTAL SCORE");

        mainJLayeredPane.add(label1);
        mainJLayeredPane.add(label2);
        mainJLayeredPane.add(label3);


        for(i=0; i<4; i++) {
            nameLabel[i] = new JLabel();
            mainJLayeredPane.add(nameLabel[i]);

            scoreNameLabel[i] = new JLabel();
            mainJLayeredPane.add(scoreNameLabel[i]);

            scoreLastLabel[i] = new JLabel("0");
            mainJLayeredPane.add(scoreLastLabel[i]);

            scoreTotalLabel[i] = new JLabel("0");
            mainJLayeredPane.add(scoreTotalLabel[i]);

            players[i] = new Player(playersName[i]);

            movedCardPanel[i] = new CardPanel();
            mainJLayeredPane.add(movedCardPanel[i]);
        }

        int x, y;

        nameLabel[0].setBounds(300, 520, 100, 20);
        nameLabel[1].setBounds(20, 80, 100, 20);
        nameLabel[2].setBounds(300, 10, 100, 20);
        nameLabel[3].setBounds(500, 435, 100, 20);

        label1.setBounds(600, 20, 100, 20);
        label2.setBounds(700, 20, 100, 20);
        label3.setBounds(800, 20, 100, 20);


        for(i=0, y = 45; i<4; i++, y += 30) {
            scoreNameLabel[i].setBounds(600, y, 100, 20);
            scoreLastLabel[i].setBounds(700, y, 50, 20);
            scoreTotalLabel[i].setBounds(800, y, 50, 20);
        }

 //       nameLabel[0].setVisible(true);

// Card for South player/ 0 Player
        for(i=0, x = 400; i<13; i++, x -= 20)
            cardPanels[i].setBounds(x, 420, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);

// Card for West Player/ 1 Player
        for(i=13, y = 340; i<26; i++, y -= 20)
            cardPanels[i].setBounds(20, y, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);

// Card for North Player/ 2 Player
        for(i=26, x = 150; i<39; i++, x += 20)
            cardPanels[i].setBounds(x, 40, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);

// Card for East Player/ 3 Player
        for(i=39, y = 100; i<52; i++, y += 20)
           cardPanels[i].setBounds(500, y, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);

        movedCardPanel[0].setBounds(280, 280, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
        movedCardPanel[1].setBounds(200, 230, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
        movedCardPanel[2].setBounds(280, 180, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
        movedCardPanel[3].setBounds(360, 230, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);

        movedCardPanel[0].setVisible(false);
        movedCardPanel[1].setVisible(false);
        movedCardPanel[2].setVisible(false);
        movedCardPanel[3].setVisible(false);


        passButton.setBounds(250, 350, 100, 25);
/*
        movedCardPanel[0].setVisible(false);
        movedCardPanel[1].setVisible(false);
        movedCardPanel[2].setVisible(false);
        movedCardPanel[3].setVisible(false);
*/

        InitFonts();
        InitColor();

        totalScore[0] = totalScore[1] = totalScore[2] = totalScore[3] = 0;

        setSize(1000, 600);
    }


    private void InitFonts()
    {
        int i;
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        label1.setFont(labelFont);
        label2.setFont(labelFont);
        label3.setFont(labelFont);

        labelFont = new Font("Comic Sans MS", Font.BOLD, 12);
        for(i=0; i<4;i++)
            scoreNameLabel[i].setFont(labelFont);

        labelFont = new Font("Calibri", Font.BOLD, 14);
        for(i=0; i<4;i++) {
            scoreLastLabel[i].setFont(labelFont);
            scoreTotalLabel[i].setFont(labelFont);
        }
    }

    private void InitColor()
    {
        Color greenColor = new Color(0, 128, 0);

        label1.setForeground(greenColor);
        label2.setForeground(greenColor);
        label3.setForeground(greenColor);
    }
    
    public void DealCard()
    {
        Card temp[] = new Card[13];

        int i, j;

        for(i=0, j=0; i<52; i++) {
            if(i != 0 && i % 13 == 0)
                players[j++].setCards(temp);
            temp[i%13] = deck.card[i];
        }
        players[j].setCards(temp);
    }

    public void ResetGame()
    {
        int i, j, k;

        deck.suffle();
        DealCard();

// -----------------------------------------------------------------------------
    players[0].printPlayer();
    players[1].printPlayer();
    players[2].printPlayer();
    players[3].printPlayer();
// -----------------------------------------------------------------------------


        for(i=0; i<13; i++)
            cardPanels[i].setCard(players[0].card[i % 13], true);

        for(i=13, j=0; i<52; i++) {
            if(i != 0 && i % 13 == 0)
                j = j + 1;
            cardPanels[i].setCard(players[j].card[i % 13], false);
            cardPanels[i].setVisible(false);
        }

        label1.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        
        for(i=0; i<4; i++) {
            scoreNameLabel[i].setVisible(false);
            scoreLastLabel[i].setVisible(false);
            scoreTotalLabel[i].setVisible(false);

            movedCardPanel[i].setVisible(false);
        }

        passButton.setVisible(false);
        passButton.setEnabled(false);

        curPhase = PHASE.PASSPHASE;
        curPassPhase = PASS_PHASE.PASSLEFT;
        playerScore[0] = playerScore[1] = playerScore[2] = playerScore[3] = 0;
        
        aniThread = new CardRepresentationThread("CardRepresentationThread Thread");
 //       autoPlayerThread = new AutoPlayerThread("Auto Player thread...");
    }

    public void ResetNextPhase()
    {
        int i, j, k;

        pitNumber = 1;

        deck.suffle();
        DealCard();

// -----------------------------------------------------------------------------
    players[0].printPlayer();
    players[1].printPlayer();
    players[2].printPlayer();
    players[3].printPlayer();
// -----------------------------------------------------------------------------


        for(i=0; i<13; i++)
            cardPanels[i].setCard(players[0].card[i % 13], true);

        for(i=13, j=0; i<52; i++) {
            if(i != 0 && i % 13 == 0)
                j = j + 1;
            cardPanels[i].setCard(players[j].card[i % 13], false);
            cardPanels[i].setVisible(false);
        }

        passButton.setVisible(true);
        passButton.setEnabled(true);

        curPhase = PHASE.PASSPHASE ;
//        goToNextPhase();
        playerScore[0] = playerScore[1] = playerScore[2] = playerScore[3] = 0;
    }
/*
    public void goToNextPhase()
    {
        if(curPassPhase == PASS_PHASE.PASSLEFT) {
            curPassPhase = PASS_PHASE.PASSRIGHT;
            passButton.setText("Pass Right");
        }
        if(curPassPhase == PASS_PHASE.PASSRIGHT) {
            curPassPhase = PASS_PHASE.PASSACCROSS;
            passButton.setText("Pass Across");
        }
        if(curPassPhase == PASS_PHASE.PASSACCROSS) {
            curPassPhase = PASS_PHASE.NOPASS;
            passButton.setText("No Pass");
        }
    }
 */

    public void actionPerformed(ActionEvent evt)
    {
        
    }

    boolean isCardCanPressed()
    {
        if(passButton.getText().equals("OK") || totalselectedCard == 3)
            return false;
        return true;
    }


    private int Winner()
    {
        int winner = firstMover;
        int hValue = movedCard.get(0).value;
        int i;

        for(i=1; i<4; i++)
            if(movedCard.get(0).suit == movedCard.get(i).suit  && movedCard.get(i).value > hValue)
                hValue = movedCard.get(i).value;
        for(i=0; i<4; i++)
            if(movedCard.get(0).suit == movedCard.get(i).suit && hValue == movedCard.get(i).value)
                winner = (firstMover + i) % 4;

        System.out.println("First Mover :" + firstMover);
        System.out.println("Winner :" + winner);
        return winner;
    }

    private int ComputePoint()
    {
        int ret = 0;

        for(int i=0; i<4; i++) {
            if(movedCard.get(i).suit == 'H')
                ret++;
            else if(movedCard.get(i).suit == 'S' && movedCard.get(i).value == Card.charToValue('Q'))
                ret = ret + 13;
        }
        return ret;
    }
/*
    private MoveFourCard(int )
    {
        
    }
*/

    private void UpdateScoreLabels()
    {
        int i;
        for(i=0 ; i<4; i++) {
            scoreLastLabel[i].setText(Integer.toString(playerScore[i]));
 //           scoreTotalLabel[i].setText();
        }
    }


    private class MouseOperationHandler extends MouseAdapter
    {
        int i, j;
        int newY = 400, oldY = 420;

        public void mousePressed(MouseEvent evt)
        {
            int i, j, k;
            int indexEventCard = INF;

            for(i=0; i<13; i++) 
                if(evt.getSource() == cardPanels[i]) {
                    indexEventCard = i;
                    break;
                }
            if(indexEventCard == INF) return;

            if(curPhase == PHASE.PASSPHASE) {
                if(isUp[indexEventCard] == true) {
                    cardPanels[indexEventCard].setBounds(cardPanels[indexEventCard].getX(), oldY, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
                    isUp[indexEventCard] = false;
                    totalselectedCard--;

                }
                else if(isUp[indexEventCard] == false &&  isCardCanPressed()){
                    cardPanels[indexEventCard].setBounds(cardPanels[indexEventCard].getX(), newY, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
                    isUp[indexEventCard] = true;
                    totalselectedCard++;
                    passCard[totalselectedCard-1] = cardPanels[indexEventCard].card;
                }
                if(totalselectedCard == 3) passButton.setEnabled(true);
                else passButton.setEnabled(false);
            }
            else if(curPhase == PHASE.MOVEPHASE) {
                cardPanels[indexEventCard].setVisible(false);
                movedCardPanel[0].setCard(cardPanels[indexEventCard].card, true);
                movedCardPanel[0].setVisible(true);

                if(movedCard.size() == 0) firstMover = 0;
                movedCard.add(cardPanels[indexEventCard].card);
                curMover = (curMover + 1) % 4;

                autoPlayerThread.myresume();
/*
                movedCard.clear();
                movedCard.add(cardPanels[indexEventCard].card);
                movedCard.add(players[1].moveCardAI(movedCard));
                movedCard.add(players[2].moveCardAI(movedCard));
                movedCard.add(players[3].moveCardAI(movedCard));

                movedCardPanel[1].setCard(movedCard.get(1), true);
                movedCardPanel[2].setCard(movedCard.get(2), true);
                movedCardPanel[3].setCard(movedCard.get(3), true);

                for(i=13; i<52; i++) 
                    if(cardPanels[i].card == movedCard.get(1) || cardPanels[i].card == movedCard.get(2) || cardPanels[i].card == movedCard.get(3))
                        cardPanels[i].setVisible(false);
                
                playerScore[Winner()] += ComputePoint();
                System.out.println(playerScore[Winner()]);

                UpdateScoreLabels();
*/
            }
        }
    }

    public void keyPressed(KeyEvent evt)
    {
        System.out.println(evt.getKeyText(evt.getKeyCode()));
    }

    public void keyReleased( KeyEvent event )
    {
        
    }

    public void keyTyped( KeyEvent event )
    {
        
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutJDialog = new javax.swing.JDialog();
        splashScreenPanel1 = new com.mm.SplashScreenPanel();
        okJButton = new javax.swing.JButton();
        mainJLayeredPane = new javax.swing.JLayeredPane();
        passButton = new javax.swing.JButton();
        backJPanel = new javax.swing.JLabel();

        okJButton.setText("OK");
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutJDialogLayout = new javax.swing.GroupLayout(aboutJDialog.getContentPane());
        aboutJDialog.getContentPane().setLayout(aboutJDialogLayout);
        aboutJDialogLayout.setHorizontalGroup(
            aboutJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splashScreenPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutJDialogLayout.createSequentialGroup()
                .addContainerGap(616, Short.MAX_VALUE)
                .addComponent(okJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        aboutJDialogLayout.setVerticalGroup(
            aboutJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutJDialogLayout.createSequentialGroup()
                .addComponent(splashScreenPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(okJButton)
                .addContainerGap())
        );

        mainJLayeredPane.setBackground(new java.awt.Color(0, 153, 0));

        passButton.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        passButton.setText("Pass Left");
        passButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passButtonActionPerformed(evt);
            }
        });
        passButton.setBounds(190, 300, 100, -1);
        mainJLayeredPane.add(passButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        backJPanel.setBackground(new java.awt.Color(0, 153, 0));
        backJPanel.setForeground(new java.awt.Color(0, 102, 0));
        backJPanel.setBounds(0, 0, 710, 470);
        mainJLayeredPane.add(backJPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainJLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainJLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void passButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passButtonActionPerformed
        int i;
        Card threeCard[] = new Card[3];

        if(passButton.getText().equals("Pass Left")) {
//        if(curPassPhase == PASS_PHASE.PASSLEFT) {
            for(i=0; i<3; i++)
                System.out.print(passCard[i].toString() + " ");
            players[0].GiveCard(passCard);
            players[1].takeCard(passCard);

            for(i=1; i<4; i++) {
                threeCard = players[i].GiveCardAI();
                players[(i+1)%4].takeCard(new Card[] { threeCard[0], threeCard[1], threeCard[2] } );
            }

            players[0].combineCard();
            players[1].combineCard();
            players[2].combineCard();
            players[3].combineCard();

            players[0].printPlayer();
            players[1].printPlayer();
            players[2].printPlayer();
            players[3].printPlayer();

            for(i=0; i<13; i++) {
                cardPanels[i].setVisible(false);
                cardPanels[i].setBounds(cardPanels[i].getX(), 420, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
                cardPanels[i].setCard(players[0].card[i], true);
                if(cardPanels[i].card == threeCard[0] || cardPanels[i].card == threeCard[1] || cardPanels[i].card == threeCard[2])
                    cardPanels[i].setBounds(cardPanels[i].getX(), 400, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
                cardPanels[i].setVisible(true);
            }

            for(i=13; i<26; i++) {
                cardPanels[i].setCard(players[1].card[i % 13], false);
                cardPanels[i].setVisible(true);
            }
            for(i=26; i<39; i++) {
                cardPanels[i].setCard(players[2].card[i % 13], false);
                cardPanels[i].setVisible(true);
            }
            for(i=39; i<52; i++) {
                cardPanels[i].setCard(players[3].card[i % 13], false);
                cardPanels[i].setVisible(true);
            }

            passButton.setText("OK");
        }
/*        else if(curPassPhase == PASS_PHASE.PASSRIGHT) {
            for(i=0; i<3; i++)
                System.out.print(passCard[i].toString() + " ");
            players[0].GiveCard(passCard);
            players[3].takeCard(passCard);

            for(i=3; i>0; i++) {
                threeCard = players[i].GiveCardAI();
                players[(i-1)%4].takeCard(new Card[] { threeCard[0], threeCard[1], threeCard[2] } );
            }

            players[0].combineCard();
            players[1].combineCard();
            players[2].combineCard();
            players[3].combineCard();

            players[0].printPlayer();
            players[1].printPlayer();
            players[2].printPlayer();
            players[3].printPlayer();

            for(i=0; i<13; i++) {
                cardPanels[i].setVisible(false);
                cardPanels[i].setBounds(cardPanels[i].getX(), 420, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
                cardPanels[i].setCard(players[0].card[i], true);
                if(cardPanels[i].card == threeCard[0] || cardPanels[i].card == threeCard[1] || cardPanels[i].card == threeCard[2])
                    cardPanels[i].setBounds(cardPanels[i].getX(), 400, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
                cardPanels[i].setVisible(true);
            }

            for(i=13; i<26; i++) {
                cardPanels[i].setCard(players[1].card[i % 13], false);
                cardPanels[i].setVisible(true);
            }
            for(i=26; i<39; i++) {
                cardPanels[i].setCard(players[2].card[i % 13], false);
                cardPanels[i].setVisible(true);
            }
            for(i=39; i<52; i++) {
                cardPanels[i].setCard(players[3].card[i % 13], false);
                cardPanels[i].setVisible(true);
            }

            passButton.setText("OK");
        }
 */
        
        else if(passButton.getText().equals("OK")) {
            for(i=0; i<13; i++) 
                cardPanels[i].setBounds(cardPanels[i].getX(), 420, CardPanel.DEFAULT_CARD_WIDTH, CardPanel.DEFAULT_CARD_HEIGHT);
            
            passButton.setText("Pass Left");
            passButton.setVisible(false);

            curPhase = PHASE.MOVEPHASE;
            for(i=0; i<isUp.length; i++)
                isUp[i] = false;


            pitNumber = 1;
            movedCard.clear();
            for(i=0; i<4; i++)
                if(players[i].hasCard(new Card("2C")) == true)
                     curMover = i;

            movedCard.clear();
            if(curMover != 0) {
                players[curMover].moveCard2C();
                movedCard.add(new Card("2C"));

                movedCardPanel[curMover].setCard(new Card("2C"), true);
                movedCardPanel[curMover].setVisible(true);
                for(i=13; i<52; i++)
                    if(cardPanels[i].card.isEqual(new Card("2C")))
                        cardPanels[i].setVisible(false);
                firstMover = curMover;
                curMover = (curMover + 1) % 4;
            }
            autoPlayerThread = new AutoPlayerThread("Auto Player thread...");
        }

        
    }//GEN-LAST:event_passButtonActionPerformed

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        aboutJDialog.dispose();
}//GEN-LAST:event_okJButtonActionPerformed


    public void NewGameAction()
    {
        ResetGame();
    }

    public void HelpTopicAction()
    {
        HelpTopicFrame frame = new HelpTopicFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

    public void AboutHeartAction()
    {
        aboutJDialog.setTitle("About mHearts v1.0");
        aboutJDialog.setSize(aboutJDialog.getPreferredSize().width, aboutJDialog.getPreferredSize().height);
        aboutJDialog.setVisible(true);
    }




    class SplashScreen extends JWindow {
        private int duration;

        public SplashScreen(int d) {
            duration = d;

            SplashScreenPanel splashPanel = new SplashScreenPanel();
            JPanel content = (JPanel) getContentPane();
            content.setBackground(Color.white);
            int width = splashPanel.getPreferredSize().width;
            int height = splashPanel.getPreferredSize().height;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;
            setBounds(x, y, width, height);

//            content.add(new JLabel("asdf"), BorderLayout.CENTER);
            content.add(splashPanel);
  //          imageLabel.setIcon(new ImageIcon(getClass().getResource()))
//            content.add(new)
            //            Color oraRed = new Color(156, 20, 20, 255);
//            content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

            setVisible(true);
            try {
                Thread.sleep(duration);
            }
            catch (Exception e) {
            }
            setVisible(false);
        }
    }

    private class AutoPlayerThread implements Runnable {
        String threadName;
        Thread t;
        boolean suspendFlag;

        AutoPlayerThread(String tName)
        {
            threadName = tName;
            t = new Thread(this, threadName);
            System.out.println("PlayerOneThread started...");
            suspendFlag = false;
            t.start();
        }

        public void run()
        {
            Card tempCard = new Card();
            int i;
            
            while( true ) {
                System.out.println("Entered into the AutoPlayerThread");
                if(pitNumber > 13) {
                    for(i=0; i<4; i++)
                        totalScore[i] += playerScore[i];
                    break;
                }
                try {
                    if(movedCard.size() != 4) {
                        if(curMover != 0) {
                            Thread.sleep(1000);
//                                System.out.println(movedCard.get(0).toString());
                            if(movedCard.size() == 0) {
                                tempCard = players[curMover].moveCardAI();
                                firstMover = curMover;
                            }
                            else tempCard = players[curMover].moveCardAI(movedCard);

                            for(i=13; i<52; i++)
                                if(tempCard.isEqual(cardPanels[i].card))
                                    cardPanels[i].setVisible(false);

                            movedCard.add(tempCard);
                            movedCardPanel[curMover].setCard(tempCard, true);
                            movedCardPanel[curMover].setVisible(true);
                            curMover = (curMover + 1) % 4;
                            if(curMover == 0 && movedCard.size() != 4) mysuspend();
                        }
                    }
                    else if(movedCard.size() == 4) {
                        Thread.sleep(2000);
                        curMover = Winner();
                        playerScore[curMover] += ComputePoint();
                        for(i=0; i<4; i++)
                            movedCardPanel[i].setVisible(false);
                        System.out.println("set NonVisible");
                        UpdateScoreLabels();
                        pitNumber++;

                        movedCard.clear();
                        if(curMover == 0) mysuspend();
                    }
                    synchronized(this) {
                        while(suspendFlag)
                            wait();
                    }
                }
                catch( InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        void mysuspend()
        {
            suspendFlag = true;
        }

        synchronized void myresume()
        {
            suspendFlag = false;
            notify();
        }
    }

    private class CardRepresentationThread implements Runnable {
        String threadName;
        Thread t;
        boolean suspendFlag;

        CardRepresentationThread(String tName)
        {
            threadName = tName;
            t = new Thread(this, threadName);
            System.out.println("CardRepresentationThread started...");
            suspendFlag = false;
            t.start();
        }

        public void run()
        {
            try {
                int i;
                Thread.sleep(10);
                for(i=0; i < 52; i++) {
                    cardPanels[i].setVisible(true);
                    Thread.sleep(5);
                    synchronized(this) {
                        while( suspendFlag )
                            wait();
                    }
                }
//                String name = JOptionPane.showInputDialog(new JFrame(), "Enter Your Name :");
                String name = JOptionPane.showInputDialog(new JFrame(), "Enter Your Name :", "Hearts", JOptionPane.PLAIN_MESSAGE);
                playersName[0] = name.toUpperCase();

                for(i=0; i<4; i++) {
                    nameLabel[i].setText(playersName[i]);
                    scoreNameLabel[i].setText(playersName[i]);
                }

                label1.setVisible(true);
                label2.setVisible(true);
                label3.setVisible(true);
                
                for(i=0; i<4; i++) {
                    scoreNameLabel[i].setVisible(true);
                    Thread.sleep(5);
                    scoreLastLabel[i].setVisible(true);
                    Thread.sleep(5);
                    scoreTotalLabel[i].setVisible(true);
                    Thread.sleep(5);
                }

                passButton.setVisible(true);
            }
            catch ( InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("CardRepresentationThread stoped.");

        }

        void mysuspend()
        {
            suspendFlag = true;
        }

        void myresume()
        {
            suspendFlag = false;
            notify();
        }




    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog aboutJDialog;
    private javax.swing.JLabel backJPanel;
    private javax.swing.JLayeredPane mainJLayeredPane;
    private javax.swing.JButton okJButton;
    private javax.swing.JButton passButton;
    private com.mm.SplashScreenPanel splashScreenPanel1;
    // End of variables declaration//GEN-END:variables

}
