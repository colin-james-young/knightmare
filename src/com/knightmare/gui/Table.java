package com.knightmare.gui;

import com.google.common.collect.Lists;
import com.knightmare.engine.board.Board;
import com.knightmare.engine.board.BoardUtils;
import com.knightmare.engine.board.Move;
import com.knightmare.engine.board.Square;
import com.knightmare.engine.pieces.Piece;
import com.knightmare.engine.players.MoveTransition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.*;

/**
 *
 * @author cjyoung
 */
public class Table {
    
    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final PlaceablePiecesPanel placeablePiecesPanel;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    
    private Board chessBoard;
    
    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 400);
    private static final Dimension SQUARE_PANEL_DIMENSION = new Dimension(10, 10);
    private static String defaultPieceImagesPath = "art/pieces/simple/";
    
    private final Color lightSquareColor = Color.decode("#FFFACD");
    private final Color darkSquareColor = Color.decode("#593E1A");
    
    public Table(){
        this.gameFrame = new JFrame("Knightmare");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.placeablePiecesPanel = new PlaceablePiecesPanel();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame.add(this.placeablePiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN file");        
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open PGN file!");
            }
        });
        fileMenu.add(openPGN);
        
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO dispose gameframe
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        
        return fileMenu;
    }
    
    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {     
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        
        preferencesMenu.addSeparator();
        
        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight legal moves", false);
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);
        return preferencesMenu;
    }
    
    
    public enum BoardDirection{
        NORMAL{
            @Override
            List<SquarePanel> traverse(List<SquarePanel> boardSquares) {
                return boardSquares;
            }
            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }         
        },
        FLIPPED{
            @Override
            List<SquarePanel> traverse(List<SquarePanel> boardSquares) {
                return Lists.reverse(boardSquares);               
            }
            @Override
            BoardDirection opposite() {
                return NORMAL;
            }           
        };       
        abstract List<SquarePanel> traverse(final List<SquarePanel> boardSquares);
        abstract BoardDirection opposite();
    }
    
    public static class MoveLog{
        private final List<Move> moves;
        
        MoveLog(){
            this.moves = new ArrayList<>();
        }
        
        public List<Move> getMoves(){
            return this.moves;
        } 
        
        public void addMove(final Move move){
            this.moves.add(move);
        }
        
        public int size(){
            return this.moves.size();
        }
        
        public void clear(){
            this.moves.clear();
        }
        
        public Move removeMove(final int index){
            return this.moves.remove(index);
        }
        
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }
    
    
    private class BoardPanel extends JPanel {
        final List<SquarePanel> boardSquares;
        
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardSquares = new ArrayList<>();
            for(int i=0; i < BoardUtils.NUM_SQUARES; i++){
                final SquarePanel squarePanel = new SquarePanel(this, i);
                this.boardSquares.add(squarePanel);
                add(squarePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        
        public void drawBoard(final Board board){
            removeAll();
            for(final SquarePanel squarePanel : boardDirection.traverse(boardSquares)){
                squarePanel.drawSquare(board);
                add(squarePanel);
            }
            validate();
            repaint();
        }
        
    }
    
    private class SquarePanel extends JPanel {
        private final int squareID;

        SquarePanel(final BoardPanel boardPanel,
                    final int squareID) {
            super(new GridBagLayout());
            this.squareID = squareID;
            setPreferredSize(SQUARE_PANEL_DIMENSION);
            assignSquareColor();
            assignSquarePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceSquare = null;
                        destinationSquare = null;
                        humanMovedPiece = null;                    
                    } else if(isLeftMouseButton(e)){
                        if(sourceSquare == null){
                            // First click
                            sourceSquare = chessBoard.getSquare(squareID);
                            humanMovedPiece = sourceSquare.getPiece();
                            if(humanMovedPiece == null){
                                sourceSquare = null;
                            }
                        } else {
                            // Second click
                            destinationSquare = chessBoard.getSquare(squareID);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceSquare.getSquareCoordinate(), destinationSquare.getSquareCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceSquare = null;
                            destinationSquare = null;
                            humanMovedPiece = null;
                        }  
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {      
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                placeablePiecesPanel.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        private void assignSquareColor() {
            if(BoardUtils.EIGHTH_RANK[this.squareID] ||
                    BoardUtils.SIXTH_RANK[this.squareID] ||
                    BoardUtils.FOURTH_RANK[this.squareID] ||
                    BoardUtils.SECOND_RANK[this.squareID]){
                setBackground(this.squareID % 2 == 0 ? lightSquareColor : darkSquareColor);
            } else if(BoardUtils.SEVENTH_RANK[this.squareID] ||
                    BoardUtils.FIFTH_RANK[this.squareID] ||
                    BoardUtils.THIRD_RANK[this.squareID] ||
                    BoardUtils.FIRST_RANK[this.squareID]){
                setBackground(this.squareID % 2 != 0 ? lightSquareColor : darkSquareColor);
            }
        }
        
        private void assignSquarePieceIcon(final Board board){
            this.removeAll();
            if(board.getSquare(this.squareID).isOccupied()){
                try {
                    final BufferedImage image = 
                            ImageIO.read(new File(defaultPieceImagesPath + board.getSquare(this.squareID).getPiece().getPieceAlliance().toString().substring(0,1) + 
                            board.getSquare(this.squareID).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException ex) {
                    Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void highlightLegals(final Board board){
            if(highlightLegalMoves){
               for(final Move move : pieceLegalMoves(board)){
                   if(move.getDestinationCoordinate() == this.squareID){
                       try {
                           add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                       } catch (IOException ex) {
                           Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
                       }
                       
                   }
               } 
            }
        }
        
        private Collection<Move> pieceLegalMoves(final Board board){
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        
        
        public void drawSquare(Board board) {
            assignSquareColor();
            assignSquarePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }
        
    }

}
