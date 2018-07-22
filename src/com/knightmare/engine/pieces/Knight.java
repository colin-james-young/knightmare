/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.knightmare.engine.board.Board;
import com.knightmare.engine.board.BoardUtils;
import com.knightmare.engine.board.Square;
import com.knightmare.engine.board.Move;
import static com.knightmare.engine.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cjyoung
 */
public class Knight extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17} ;

    public Knight(final int piecePosition,
                  final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }

    public Knight(final int piecePosition,
                  final Alliance pieceAlliance,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
       
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            final int candidateDestCoord = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidSquareCoordinate(candidateDestCoord)){
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
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
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currPos] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset== 6 || candidateOffset== 15);
    }
    private static boolean isSecondColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currPos] && (candidateOffset == -10 || candidateOffset == 6);
    }
    private static boolean isSeventhColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currPos] && (candidateOffset == -6 || candidateOffset == 10);
    }
    private static boolean isEighthColumnExclusion(final int currPos, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currPos] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);
    }
    
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public Knight changeToPlaceable() {
        return new Knight(-1, this.getPieceAlliance().opposite());
    }

}
