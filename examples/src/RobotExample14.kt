import org.jointheleague.graphical.robot.Robot
import org.jointheleague.graphical.robot.RobotWindow
import java.awt.Color
import java.awt.geom.PathIterator
import java.io.File
import java.lang.RuntimeException
import java.lang.UnsupportedOperationException

private val floatRegex = """[-+]?\d*\.?\d+""".toRegex()

private class SequencePathIterator(lines: Sequence<String>) : PathIterator {

    val iterator = lines
            .filter { !it.isBlank() }
            .map { it.trim() }
            .iterator()
    var done: Boolean = !iterator.hasNext()
    var currentLine = if (iterator.hasNext()) iterator.next() else ""
    var refPointX: Float = 0F
    var refPointY: Float = 0F

    override fun next() {
        if (iterator.hasNext()) {
            currentLine = iterator.next()
        } else {
            done = true
        }
    }

    override fun getWindingRule(): Int = PathIterator.WIND_EVEN_ODD

    override fun currentSegment(coords: FloatArray?): Int {

        val coords_: FloatArray = coords ?: FloatArray(6)

        floatRegex.findAll(currentLine).forEachIndexed { i, matchResult ->
            coords_[i] = matchResult.value.toFloat()
        }

        return when (currentLine[0]) {
            'M' -> {
                refPointX = coords_[0]
                refPointY = coords_[1]
                PathIterator.SEG_MOVETO
            }
            'L' -> {
                refPointX = coords_[0]
                refPointY = coords_[1]
                PathIterator.SEG_LINETO
            }
            'l' -> {
                for (i in 0..1) {
                    coords_[i] +=  if (i % 2 == 0) refPointX else refPointY
                }
                refPointX = coords_[0]
                refPointY = coords_[1]
                PathIterator.SEG_LINETO
            }
            'Q' -> {
                refPointX = coords_[2]
                refPointY = coords_[3]
                PathIterator.SEG_QUADTO
            }
            'q' -> {
                for (i in 0..3) {
                    coords_[i] +=  if (i % 2 == 0) refPointX else refPointY
                }
                refPointX = coords_[2]
                refPointY = coords_[3]
                PathIterator.SEG_QUADTO
            }
            'C' -> {
                refPointX = coords_[4]
                refPointY = coords_[5]
                PathIterator.SEG_CUBICTO
            }
            'c' -> {
                for (i in 0..5) {
                    coords_[i] +=  if (i % 2 == 0) refPointX else refPointY
                }
                refPointX = coords_[4]
                refPointY = coords_[5]
                PathIterator.SEG_CUBICTO
            }
            'Z', 'z' -> PathIterator.SEG_CLOSE
            else -> throw RuntimeException()
        }
    }

    override fun currentSegment(coords: DoubleArray?): Int {
        throw UnsupportedOperationException()
    }

    override fun isDone(): Boolean = done
}

fun main() {

    val rob = Robot()
    RobotWindow.getInstance().setWinColor(Color.WHITE)
    rob.setSpeed(20)
    rob.penColor = Color.DARK_GRAY
    rob.penWidth = 1
    rob.miniaturize()
    rob.penDown()
    File("examples/res/homer-simpson.txt").useLines {
        rob.followPath(SequencePathIterator(it), true)
    }
    rob.sleep(1000)
    rob.hide()
}

