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
public class King extends Piece {

    private static final int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

    
    public King(final int piecePosition,
                final Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
    }

    public King(final int piecePosition,
                final Alliance pieceAlliance,
                final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATES){
            final int candidateDestCoord = this.piecePosition + currentCandidateOffset;
            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
               continue; 
            }
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
                }
            }
        }
        
        return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
    
    private static boolean isFirstColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currPos] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset== 7);
    }
    
    private static boolean isEighthColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currPos] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset== 9);
    }
    
    @Override
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public King changeToPlaceable() {
        return new King(-1, this.getPieceAlliance().opposite());
    }

}
