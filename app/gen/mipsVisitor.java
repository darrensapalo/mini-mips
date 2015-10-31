// Generated from C:/Users/Darren/git/mini-mips/app\mips.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link mipsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface mipsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link mipsParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(mipsParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(mipsParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstruction(mipsParser.InstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#lbl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLbl(mipsParser.LblContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#argumentlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentlist(mipsParser.ArgumentlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(mipsParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(mipsParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(mipsParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(mipsParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(mipsParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(mipsParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(mipsParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link mipsParser#opcode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpcode(mipsParser.OpcodeContext ctx);
}