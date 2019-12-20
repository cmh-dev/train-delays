import org.jetbrains.annotations.TestOnly
import org.junit.Test

class FailingTest {

    @Test
    fun `failed`() {
        assert(false)
    }

}