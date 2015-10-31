// Generated from C:/Users/Darren/git/mini-mips/app\mips.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link mipsParser}.
 */
public interface mipsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link mipsParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(mipsParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(mipsParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(mipsParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(mipsParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(mipsParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(mipsParser.InstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#lbl}.
	 * @param ctx the parse tree
	 */
	void enterLbl(mipsParser.LblContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#lbl}.
	 * @param ctx the parse tree
	 */
	void exitLbl(mipsParser.LblContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#argumentlist}.
	 * @param ctx the parse tree
	 */
	void enterArgumentlist(mipsParser.ArgumentlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#argumentlist}.
	 * @param ctx the parse tree
	 */
	void exitArgumentlist(mipsParser.ArgumentlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(mipsParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(mipsParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(mipsParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(mipsParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(mipsParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(mipsParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(mipsParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(mipsParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(mipsParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(mipsParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(mipsParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(mipsParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(mipsParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(mipsParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link mipsParser#opcode}.
	 * @param ctx the parse tree
	 */
	void enterOpcode(mipsParser.OpcodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link mipsParser#opcode}.
	 * @param ctx the parse tree
	 */
	void exitOpcode(mipsParser.OpcodeContext ctx);
}