/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.knightmare.engine.board.Board;
import com.knightmare.engine.board.BoardUtils;
import com.knightmare.engine.board.Move;
import static com.knightmare.engine.board.Move.*;
import com.knightmare.engine.board.Square;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cjyoung
 */
public class Rook extends Piece{
    
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -8, -1, 1, 8 };
    
    public Rook(final int piecePosition,
                final Alliance pieceAlliance) {
        super(PieceType.ROOK, piecePosition, pieceAlliance, true);
    }
    
    public Rook(final int piecePosition,
                final Alliance pieceAlliance,
                final boolean isFirstMove){
        super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int candidateCoordinateOffset:CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestCoord = this.piecePosition;
            while(BoardUtils.isValidSquareCoordinate(candidateDestCoord)){   
                if(isFirstColumnExclusion(candidateDestCoord, candidateCoordinateOffset) ||
                        isEighthColumnException(candidateDestCoord, candidateCoordinateOffset)){
                    break;
                }
                candidateDestCoord += candidateCoordinateOffset;                
                if(BoardUtils.isValidSquareCoordinate(candidateDestCoord)){
                    
                    final Square candidateDestinationSquare = board.getSquare(candidateDestCoord);
                    if(!candidateDestinationSquare.isOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestCoord));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationSquare.getPiece();
                        final Alliance destPieceAlliance = pieceAtDestination.getPieceAlliance();
                        if(this.pieceAlliance != destPieceAlliance){
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestCoord, pieceAtDestination));
                        }
                        break;
                    }   
                }
            }
        }
        
            return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }
    
    private static boolean isFirstColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currPos] && (candidateOffset == -1);
    }
    
    private static boolean isEighthColumnException(final int currPos, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currPos] && (candidateOffset == 1);
    }
    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public Rook changeToPlaceable() {
        return new Rook(-1, this.getPieceAlliance().opposite());
    }

}
