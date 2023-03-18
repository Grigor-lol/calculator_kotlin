package mmcs.assignment3_calculator.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class CalculatorViewModel: BaseObservable(), Calculator {
    override var display = ObservableField<String>()

    private var left = 0.0
    private var right = 0.0
    private var b = false
    private var started = false
    private var opPressed = false
    private var eqPressed = false
    private var divideByZero = false
    private var op:Operation = Operation.NIL

    override fun addDigit(dig: Int)
    {
        this.started = true
        this.opPressed = false

        if (divideByZero) {
            display.set("")
            divideByZero = false
            op = Operation.NIL
            left = 0.0
            right = 0.0
        }

        if(this.b){
            display.set("")
            b = false
        }

        if (this.eqPressed){
            display.set("")
            eqPressed = false
        }
        if ("${display.get()}".isEmpty()) {
            display.set(dig.toString())
            return
        }

        if (display.get() != null)
        {
            val number = display.get() + dig
            display.set(number)
            return
        }

        display.set(dig.toString())
    }

    override fun addPoint() {
        if (display.get() == "" || display.get() == null /*|| numberHavePoint*/)
        {
            return
        }

        if(display.get()!!.contains('.')) {
            return
        }
        display.set("${display.get()}.")
    }

    override fun addOperation(op: Operation) {
        if (!started || divideByZero) return
        this.b = true
        if (this.opPressed ){
            when (op){
                Operation.ADD -> this.op = Operation.ADD
                Operation.SUB -> this.op = Operation.SUB
                Operation.MUL -> this.op = Operation.MUL
                Operation.DIV -> this.op = Operation.DIV
            }
            return
        }
        if (this.op!= Operation.NIL) {
            this.right = display.get()?.toDouble() ?: 0.0
            this.compute()
        }
        else {
            this.left = display.get()?.toDouble() ?: 0.0
        }

        when (op){
            Operation.ADD -> this.op = Operation.ADD
            Operation.SUB -> this.op = Operation.SUB
            Operation.MUL -> this.op = Operation.MUL
            Operation.DIV -> this.op = Operation.DIV
        }
        this.opPressed = true

    }

    override fun compute() {
        var res = 0.0
        when (this.op){
            Operation.ADD -> {
                res = this.left + this.right
            }
            Operation.SUB -> {
                res = this.left - this.right
            }
            Operation.MUL -> {
                res = this.left * this.right
            }
            Operation.DIV -> {
                if (this.right == 0.0) {
                    display.set("Divide by zero!")
                    divideByZero = true
                    return
                }
                res = this.left / this.right
            }
        }
        this.left = res
        display.set(res.toString())


    }

    override fun clear() {
        if (display.get() == null || display.get() == "") {
            return
        }

        println("${display.get()}".dropLast(1))

        var text = "${display.get()}".dropLast(1)


        display.set(text)
    }

    override fun reset() {
        started = false
        display.set("")
        this.right = 0.0
        this.left = 0.0
        this.op = Operation.NIL
    }

    override fun eval() {
        if (!started || divideByZero || opPressed) return
        this.opPressed = false
        this.eqPressed = true
        this.right = display.get()?.toDouble() ?: 0.0
        this.compute()
        this.right = 0.0
        this.op = Operation.NIL
    }
}

