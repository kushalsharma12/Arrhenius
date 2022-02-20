package com.kushalsharma.arrhenius

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kushalsharma.arrhenius.databinding.FragmentWalletBinding
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.io.File
import java.math.BigDecimal
import java.security.Provider
import java.security.Security


class WalletFragment : Fragment() {

    private lateinit var web3j: Web3j
    private lateinit var file: File
    private var walletName: String? = null
    private lateinit var credentials: Credentials

    private var _binding: FragmentWalletBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //enter infura api key
        web3j =
            Web3j.build(HttpService("https://kovan.infura.io/v3/56a78f3f16e64d8788090f559dc627ce"))

        setUpBouncyCastle()

        val main = MainActivity()
        val filsdir = main.filesDir.toString()

        val ethWalletPath = binding.walletpath.text.toString()
        file = File(filsdir + ethWalletPath)

        if (!file.mkdirs()) {
            file.mkdirs()
        } else {
            Toast.makeText(
                this.requireContext(), "Directory already created",
                Toast.LENGTH_LONG
            ).show()

        }


        // text_address view  Clicked
        binding.textAddress.setOnClickListener {
            val address = binding.textAddress.text.toString()
            (activity as MainActivity).copyToClipBoard(address)
            (activity as MainActivity).showToast("Copied to clipboard")
        }
        binding.wallet.setOnClickListener {
            val address = binding.textAddress.text.toString()
            (activity as MainActivity).copyToClipBoard(address)
            (activity as MainActivity).showToast("Copied to clipboard")
        }



        binding.createWalletBtn.setOnClickListener {
            val password = binding.password.text.toString()

            try {
                walletName = WalletUtils.generateLightNewWalletFile(password, file)
                (activity as MainActivity).showToast("Wallet- $walletName is generated.")
                credentials = WalletUtils.loadCredentials(password, "$file/$walletName")
                binding.textAddress.setText(credentials.address)

            } catch (e: Exception) {
                (activity as MainActivity).showToast("failed")
            }

        }

        binding.buttonsconnect.setOnClickListener {
            web3j =
                Web3j.build(HttpService("https://kovan.infura.io/v3/56a78f3f16e64d8788090f559dc627ce"))

            (activity as MainActivity).showToast("Now connecting to Ethereum network.")

            try {
                //if the client version has an error the user will not gain access if successful the user will get connnected
                val clientVersion: Web3ClientVersion = web3j.web3ClientVersion().sendAsync().get()
                if (!clientVersion.hasError()) {
                    (activity as MainActivity).showToast("Connected!")

                } else {
                    (activity as MainActivity).showToast(clientVersion.error.message)
                }

            } catch (e: Exception) {
                (activity as MainActivity).showToast(e.message)

            }


        }

        binding.buttonbalance.setOnClickListener {
            try {
                val balanceWei: EthGetBalance = web3j.ethGetBalance(
                    binding.textAddress.toString(),
                    DefaultBlockParameterName.LATEST
                ).sendAsync()
                    .get()
                binding.textBalance.text = getString(R.string.your_balance) + balanceWei.balance
            } catch (e: java.lang.Exception) {
                (activity as MainActivity).showToast("balance failed")
            }


        }

        binding.buttonmake.setOnClickListener {
//        val value: Int = Integer.parseInt(ethvalue.text.toString())
            val value = binding.ethValueTxt.text.toString()

            try {
                val receipt = Transfer.sendFunds(
                    web3j,
                    credentials,
                    "0x55555513537ec7f03a0af928bce4b200e6d677dd",
//                BigDecimal.valueOf(value.toLong()),
                    BigDecimal(value),
                    Convert.Unit.WEI
                ).send()
                Toast.makeText(
                    this.requireContext(),
                    "Transaction successful: " + receipt.transactionHash,
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: java.lang.Exception) {
                (activity as MainActivity).showToast(e.message.toString())
                Log.d("transactionError", e.message.toString())
            }

        }


        return root
    }


    // setup security provider
    private fun setUpBouncyCastle() {

        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up a provider  when it's used for the first time.
            return

        if (provider.javaClass == BouncyCastleProvider::class.java) {
            return
        }
        //There is a possibility  the bouncy castle registered by android may not have all ciphers
        //so we  substitute with the one bundled in the app.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}