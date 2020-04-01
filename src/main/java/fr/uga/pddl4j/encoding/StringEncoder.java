/*
 * Copyright (c) 2010 by Damien Pellier <Damien.Pellier@imag.fr>.
 *
 * This file is part of PDDL4J library.
 *
 * PDDL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PDDL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PDDL4J.  If not, see <http://www.gnu.org/licenses/>
 */

package fr.uga.pddl4j.encoding;

import fr.uga.pddl4j.operators.Action;
import fr.uga.pddl4j.operators.Method;
import fr.uga.pddl4j.operators.BitExp;
import fr.uga.pddl4j.operators.CondBitExp;
import fr.uga.pddl4j.operators.TaskNetwork;
import fr.uga.pddl4j.parser.Connective;
import fr.uga.pddl4j.parser.Symbol;
import fr.uga.pddl4j.util.BitMatrix;

import java.io.Serializable;
import java.util.BitSet;
import java.util.List;

/**
 * This class implements the methods needed to convert the compact encoded objects used in the
 * encoding into string representation.
 *
 * @author D. Pellier
 * @version 1.0 - 11.06.2010
 */
final class StringEncoder implements Serializable {

    /**
     * The serial version id of the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default constructor with a private access to prevent instance creation.
     */
    private StringEncoder() {
    }

    /**
     * Returns a string representation of the specified operator.
     *
     * @param op         the operator to print.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks     the table of functions.
     * @return a string representation of the specified operator.
     */
    static String toString(final IntAction op, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks) {
        final StringBuilder str = new StringBuilder();
        str.append("Action ").append(op.getName()).append("\n");
        str.append("Instantiations:\n");
        for (int i = 0; i < op.getArity(); i++) {
            final int index = op.getValueOfParameter(i);
            final String type = types.get(op.getTypeOfParameters(i));
            if (index == -1) {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ? \n");
            } else {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ")
                    .append(constants.get(index)).append(" \n");
            }
        }
        str.append("Preconditions:\n").append(toString(op.getPreconditions(), constants, types, predicates, functions, tasks))
            .append("\n")
            .append("Effects:\n")
            .append(toString(op.getEffects(), constants, types, predicates, functions, tasks))
            .append("\n");
        return str.toString();
    }

    /**
     * Returns a string representation of the specified method.
     *
     * @param meth       the method to print.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @return a string representation of the specified method.
     */
    static String toString(final IntMethod meth, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks) {
        final StringBuilder str = new StringBuilder();
        str.append("Method ").append(meth.getName()).append("\n");
        str.append("Instantiations:\n");
        for (int i = 0; i < meth.getArity(); i++) {
            final int index = meth.getValueOfParameter(i);
            final String type = types.get(meth.getTypeOfParameters(i));
            if (index == -1) {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ? \n");
            } else {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ")
                    .append(constants.get(index)).append(" \n");
            }
        }
        str.append("Task: ").append(toString(meth.getTask(), constants, types, predicates, functions, tasks))
            .append("\n");
        str.append("Preconditions:\n").append(toString(meth.getPreconditions(), constants, types, predicates, functions, tasks))
            .append("\n");
        str.append("Subtasks:\n")
            .append(toString(meth.getSubTasks(), constants, types, predicates, functions, tasks))
            .append("\n");
        str.append("Ordering:\n")
            .append(toString(meth.getOrderingConstraints(), constants, types, predicates, functions, tasks))
            .append("\n");
        return str.toString();
    }

    /**
     * Returns a string representation of the specified task network.
     *
     * @param tn         the task network to print.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @return a string representation of the specified method.
     */
    static String toString(final IntTaskNetwork tn, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks) {
        final StringBuilder str = new StringBuilder();
        str.append("Tasks:\n")
            .append(toString(tn.getTasks(), constants, types, predicates, functions, tasks))
            .append("\n");
        str.append("Ordering constraints:\n")
            .append(toString(tn.getOrderingConstraints(), constants, types, predicates, functions, tasks))
            .append("\n");
        return str.toString();
    }

    /**
     * Returns a string representation of the specified task network.
     *
     * @param tn            the task network to print.
     * @param constants     the table of constants.
     * @param types         the table of types.
     * @param predicates    the table of predicates.
     * @param functions     the table of functions.
     * @param tasks         the table of tasks.
     * @param relevantTasks the table of relevant tasks.
     * @return a string representation of the specified method.
     */
    static String toString(final TaskNetwork tn, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks,
                           final List<IntExp> relevantTasks) {
        final StringBuilder str = new StringBuilder();
        str.append("Tasks:\n");
        if (tn.getTasks().length == 0) {
            str.append(" ()\n");
        } else {
            for (int i = 0; i < tn.getTasks().length; i++) {
                str.append(" " + Symbol.DEFAULT_TASK_ID_SYMBOL + i + ": ");
                str.append(StringEncoder.toString(relevantTasks.get(tn.getTasks()[i]), constants, types, predicates,
                    functions, tasks)).append("\n");
            }
        }
        str.append("Ordering constraints:\n");
        if (tn.getOrderingConstraints().cardinality() == 0) {
            str.append(" ()");
        } else {
            BitMatrix constraints = tn.getOrderingConstraints();
            int index = 0;
            for (int r = 0; r < constraints.rows(); r++) {
                BitSet row = constraints.getRow(r);
                for (int c = row.nextSetBit(0); c >= 0; c = row.nextSetBit(c + 1)) {
                    str.append(" C").append(index).append(": ").append(Symbol.DEFAULT_TASK_ID_SYMBOL + r).append(" ");
                    str.append(Connective.LESS_ORDERING_CONSTRAINT.getImage()).append(" ");
                    str.append(Symbol.DEFAULT_TASK_ID_SYMBOL + c).append("\n");
                    index++;
                }
            }
        }
        return str.toString();
    }

    /**
     * Returns a string representation of an expression.
     *
     * @param exp        the expression.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @return a string representation of the specified expression.
     */
    static String toString(final IntExp exp, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks) {
        return StringEncoder.toString(exp, constants, types, predicates, functions, tasks, " ");
    }

    /**
     * Returns a string representation of an expression.
     *
     * @param exp        the expression.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @param separator  the string separator between predicate symbol and arguments.
     * @return a string representation of the specified expression.
     */
    static String toString(final IntExp exp, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks,
                           final String separator) {
        return StringEncoder.toString(exp, constants, types, predicates, functions, tasks, "", separator);
    }

    /**
     * Returns a string representation of an expression.
     *
     * @param exp        the expression.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @param baseOffset the offset white space from the left used for indentation.
     * @param separator  the string separator between predicate symbol and arguments.
     * @return a string representation of the specified expression node.
     */
    private static String toString(final IntExp exp, final List<String> constants,
                                   final List<String> types, final List<String> predicates,
                                   final List<String> functions, final List<String> tasks,
                                   String baseOffset, final String separator) {
        final StringBuilder str = new StringBuilder();
        switch (exp.getConnective()) {
            case ATOM:
                str.append("(");
                str.append(predicates.get(exp.getPredicate()));
                int[] args = exp.getArguments();
                for (int index : args) {
                    if (index < 0) {
                        str.append(" ").append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(-index - 1);
                    } else {
                        str.append(" ").append(constants.get(index));
                    }
                }
                str.append(")");
                break;
            case FN_HEAD:
                str.append("(").append(functions.get(exp.getPredicate()));
                args = exp.getArguments();
                for (int index : args) {
                    if (index < 0) {
                        str.append(" ").append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(-index - 1);
                    } else {
                        str.append(" ").append(constants.get(index));
                    }
                }
                str.append(")");
                break;
            case TASK:
                str.append("(");
                if (exp.getTaskID() != IntExp.DEFAULT_TASK_ID) {
                    str.append(Symbol.DEFAULT_TASK_ID_SYMBOL);
                    str.append(exp.getTaskID());
                    str.append(" (");
                }
                str.append(tasks.get(exp.getPredicate()));
                args = exp.getArguments();
                for (int index : args) {
                    if (index < 0) {
                        str.append(" ").append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(-index - 1);
                    } else {
                        str.append(" ").append(constants.get(index));
                    }
                }
                if (exp.getTaskID() != IntExp.DEFAULT_TASK_ID) {
                    str.append(")");
                }
                str.append(")");
                break;
            case EQUAL_ATOM:
                str.append("(").append("=");
                args = exp.getArguments();
                for (int index : args) {
                    if (index < 0) {
                        str.append(" ").append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(-index - 1);
                    } else {
                        str.append(" ").append(constants.get(index));
                    }
                }
                str.append(")");
                break;
            case AND:
            case OR:
                String offsetOr = baseOffset + "  ";
                str.append("(");
                str.append(exp.getConnective().getImage());
                str.append(" ");
                if (!exp.getChildren().isEmpty()) {
                    for (int i = 0; i < exp.getChildren().size() - 1; i++) {
                        str.append(StringEncoder.toString(exp.getChildren().get(i), constants, types, predicates,
                            functions, tasks, offsetOr)).append("\n").append(offsetOr);
                    }
                    str.append(StringEncoder.toString(exp.getChildren().get(
                        exp.getChildren().size() - 1), constants, types, predicates, functions, tasks, offsetOr));
                }
                str.append(")");
                break;
            case FORALL:
            case EXISTS:
                String offsetEx = baseOffset + baseOffset + "  ";
                str.append(" (").append(exp.getConnective().getImage())
                    .append(" (").append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(-exp.getVariable() - 1).append(" - ")
                    .append(types.get(exp.getType())).append(")\n").append(offsetEx);
                if (exp.getChildren().size() == 1) {
                    str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates,
                        functions, tasks, offsetEx));
                }
                str.append(")");
                break;
            case NUMBER:
                str.append(exp.getValue());
                break;
            case F_EXP:
                str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates,
                    functions, tasks, baseOffset));
                break;
            case F_EXP_T:
            case TRUE:
            case FALSE:
                str.append(exp.getConnective());
                break;
            case TIME_VAR:
                break;
            case FN_ATOM:
            case WHEN:
            case DURATION_ATOM:
            case LESS:
            case LESS_OR_EQUAL:
            case EQUAL:
            case GREATER:
            case GREATER_OR_EQUAL:
            case ASSIGN:
            case INCREASE:
            case DECREASE:
            case SCALE_UP:
            case SCALE_DOWN:
            case MUL:
            case DIV:
            case MINUS:
            case PLUS:
            case SOMETIME_AFTER:
            case SOMETIME_BEFORE:
                str.append("(");
                str.append(exp.getConnective().getImage());
                str.append(" ");
                str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates,
                    functions, tasks, baseOffset));
                str.append(" ");
                str.append(StringEncoder.toString(exp.getChildren().get(1), constants, types, predicates,
                    functions, tasks, baseOffset));
                str.append(")");
                break;
            case AT_START:
            case AT_END:
            case OVER_ALL:
            case MINIMIZE:
            case MAXIMIZE:
            case UMINUS:
            case NOT:
            case ALWAYS:
                str.append("(");
                str.append(exp.getConnective().getImage());
                str.append(" ");
                str.append(StringEncoder.toString(exp.getChildren().get(0), constants, types, predicates,
                    functions, tasks, baseOffset));
                str.append(")");
                break;
            case IS_VIOLATED:
                str.append("(");
                str.append(exp.getConnective().getImage());
                str.append(")");
                break;
            case LESS_ORDERING_CONSTRAINT:
                str.append("(");
                str.append(Symbol.DEFAULT_TASK_ID_SYMBOL);
                str.append(exp.getChildren().get(0).getTaskID());
                str.append(" ");
                str.append(exp.getConnective().getImage());
                str.append(" ");
                str.append(Symbol.DEFAULT_TASK_ID_SYMBOL);
                str.append(exp.getChildren().get(1).getTaskID());
                str.append(")");
                break;
            default:
                str.append("DEFAULT");
                break;
        }
        return str.toString();
    }

    /**
     * Returns a string representation of a specified operator.
     *
     * @param action         the operator.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @param relevants  the table of relevant facts.
     * @return a string representation of the specified operator.
     */
    static String toString(final Action action, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions,
                           final List<String> tasks, final List<IntExp> relevants) {
        StringBuilder str = new StringBuilder();
        str.append("Action ").append(action.getName()).append("\n").append("Instantiations:\n");
        for (int i = 0; i < action.getArity(); i++) {
            final int index = action.getValueOfParameter(i);
            final String type = types.get(action.getTypeOfParameters(i));
            if (index == -1) {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ? \n");
            } else {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ")
                    .append(constants.get(index)).append(" \n");
            }
        }
        str.append("Preconditions:\n").append(StringEncoder.toString(action.getPreconditions(), constants, types,
            predicates, functions, tasks, relevants)).append("\n").append("Effects:\n");
        for (CondBitExp condExp : action.getCondEffects()) {
            str.append(StringEncoder.toString(condExp, constants, types, predicates, functions, tasks, relevants))
                .append("\n");
        }
        return str.toString();
    }

    /**
     * Returns a string representation of the specified method.
     *
     * @param method        the method to print.
     * @param constants     the table of constants.
     * @param types         the table of types.
     * @param predicates    the table of predicates.
     * @param functions     the table of functions.
     * @param tasks         the table of tasks.
     * @param relevantTasks the table of relevant tasks.
     * @return a string representation of the specified method.
     */
    static String toString(final Method method, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks,
                           final  List<IntExp> relevantFacts, final List<IntExp> relevantTasks) {
        final StringBuilder str = new StringBuilder();
        str.append("Method ").append(method.getName()).append("\n").append("Instantiations:\n");
        for (int i = 0; i < method.getArity(); i++) {
            final int index = method.getValueOfParameter(i);
            final String type = types.get(method.getTypeOfParameters(i));
            if (index == -1) {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ? \n");
            } else {
                str.append(Symbol.DEFAULT_VARIABLE_SYMBOL).append(i).append(" - ").append(type).append(" : ")
                    .append(constants.get(index)).append(" \n");
            }
        }
        str.append("Preconditions:\n").append(StringEncoder.toString(method.getPreconditions(), constants, types,
            predicates, functions, tasks, relevantFacts)).append("\n");
        str.append(StringEncoder.toString(method.getTaskNetwork(), constants, types, predicates, functions, tasks,
            relevantTasks));
        return str.toString();
    }

    /**
     * Returns a string representation of a bit expression.
     *
     * @param exp        the expression.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @param relevants  the table of relevant facts.
     * @return a string representation of the specified expression.
     */
    static String toString(BitExp exp, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks,
                           final List<IntExp> relevants) {
        final StringBuilder str = new StringBuilder("(and");
        final BitSet positive = exp.getPositive();
        for (int i = positive.nextSetBit(0); i > 0; i = positive.nextSetBit(i + 1)) {
            str.append(" ").append(StringEncoder.toString(relevants.get(i), constants, types, predicates, functions, tasks))
                .append("\n");
        }
        final BitSet negative = exp.getNegative();
        for (int i = negative.nextSetBit(0); i >= 0; i = negative.nextSetBit(i + 1)) {
            str.append(" (not ").append(StringEncoder.toString(relevants.get(i), constants, types, predicates,
                functions, tasks)).append(")\n");
        }
        str.append(")");
        return str.toString();
    }

    /**
     * Returns a string representation of a bit state.
     *
     * @param bitState   the state.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @param relevants  the table of relevant facts.
     * @return a string representation of the specified expression.
     */
    static String toString(State bitState, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks,
                           final List<IntExp> relevants) {
        final StringBuilder str = new StringBuilder("(and");
        for (int i = bitState.nextSetBit(0); i >= 0; i = bitState.nextSetBit(i + 1)) {
            str.append(" ").append(StringEncoder.toString(relevants.get(i), constants, types, predicates, functions, tasks))
                .append("\n");
        }
        str.append(")");
        return str.toString();
    }

    /**
     * Returns a string representation of a conditional bit expression.
     *
     * @param exp        the conditional expression.
     * @param constants  the table of constants.
     * @param types      the table of types.
     * @param predicates the table of predicates.
     * @param functions  the table of functions.
     * @param tasks      the table of tasks.
     * @param relevants  the table of relevant facts.
     * @return a string representation of the specified expression.
     */
    static String toString(CondBitExp exp, final List<String> constants, final List<String> types,
                           final List<String> predicates, final List<String> functions, final List<String> tasks,
                           final List<IntExp> relevants) {
        StringBuilder str = new StringBuilder();
        if (exp.getCondition().isEmpty()) {
            str.append(StringEncoder.toString(exp.getEffects(), constants, types, predicates, functions, tasks, relevants));
        } else {
            str.append("(when ").append(StringEncoder.toString(exp.getCondition(), constants, types, predicates,
                functions, tasks, relevants)).append("\n").append(StringEncoder.toString(exp.getEffects(), constants, types,
                predicates, functions, tasks, relevants)).append(")");
        }
        return str.toString();
    }

    /**
     * Returns a short string representation of the specified operator, i.e., its name and its
     * instantiated parameters.
     *
     * @param op        the operator.
     * @param constants the table of constants.
     * @return a string representation of the specified operator.
     */
    static String toShortString(final IntAction op, final List<String> constants) {
        final StringBuilder str = new StringBuilder();
        str.append(op.getName());
        for (int i = 0; i < op.getArity(); i++) {
            final int index = op.getValueOfParameter(i);
            if (index == -1) {
                str.append(" ?");
            } else {
                str.append(" ").append(constants.get(index));
            }
        }
        return str.toString();
    }

    /**
     * Returns a short string representation of the specified operator, i.e., its name and its
     * instantiated parameters.
     *
     * @param op        the operator.
     * @param constants the table of constants.
     * @return a string representation of the specified operator.
     */
    static String toShortString(final Action op, final List<String> constants) {
        final StringBuilder str = new StringBuilder();
        str.append(op.getName());
        for (int i = 0; i < op.getArity(); i++) {
            final int index = op.getValueOfParameter(i);
            if (index == -1) {
                str.append(" ?");
            } else {
                str.append(" ").append(constants.get(index));
            }
        }
        return str.toString();
    }

}
