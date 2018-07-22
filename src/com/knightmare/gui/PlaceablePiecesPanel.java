/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.gui;

import com.google.common.primitives.Ints;
import com.knightmare.engine.board.Move;
import com.knightmare.engine.pieces.Piece;
import static com.knightmare.gui.Table.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author cjyoung
 */
public class PlaceablePiecesPanel extends JPanel{
    
    private final JPanel northPanel;
    private final JPanel southPanel;
    
    private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private static final Dimension PLACEABLE_PIECES_DIMENSION = new Dimension(40, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    
    public PlaceablePiecesPanel(){
        super(new BorderLayout());
        setBackground(Color.decode("0xFDF5E6"));
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(PLACEABLE_PIECES_DIMENSION);
    } 
    
    public void redo(final MoveLog moveLog){
        southPanel.removeAll();
        northPanel.removeAll();
        
        final List<Piece> whitePlaceablePieces = new ArrayList<>();
        final List<Piece> blackPlaceablePieces = new ArrayList<>();
        
        for(final Move move : moveLog.getMoves()){
            if(move.isAttack()){
                final Piece placeablePiece = move.getAttackedPiece().changeToPlaceable();
                if(placeablePiece.getPieceAlliance().isWhite()){
                    whitePlaceablePieces.add(placeablePiece);
                } else if(placeablePiece.getPieceAlliance().isBlack()){
                    blackPlaceablePieces.add(placeablePiece);
                } else {
                    throw new RuntimeException("Invalid piece alliance");
                }
            }
        }
        
        Collections.sort(whitePlaceablePieces, new Comparator<Piece>(){
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
            
        });
        Collections.sort(blackPlaceablePieces, new Comparator<Piece>(){
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
            
        });
        
        for(final Piece placeablePiece : whitePlaceablePieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art/pieces/simple/" +
                        placeablePiece.getPieceAlliance().toString().substring(0, 1) + 
                        placeablePiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
            } catch(final IOException e){
                e.printStackTrace();
            }
        }
        for(final Piece placeablePiece : blackPlaceablePieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art/pieces/simple/" +
                        placeablePiece.getPieceAlliance().toString().substring(0, 1) + 
                        placeablePiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            } catch(final IOException e){
                e.printStackTrace();
            }
        }
        
        validate();
    }
}
