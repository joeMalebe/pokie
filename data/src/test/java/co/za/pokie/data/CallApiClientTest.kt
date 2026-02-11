package co.za.pokie.data

import co.za.pokie.data.util.ApiResult
import co.za.pokie.data.util.callApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException

class CallApiClientTest {

    @Test
    fun `when connectException then return no Internet error result`() = runTest {
       val result = callApiClient<String>( { throw ConnectException("") })

        assertTrue(result is ApiResult.NoInternetError)
    }
}