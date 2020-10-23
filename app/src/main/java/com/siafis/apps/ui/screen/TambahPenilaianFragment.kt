package com.siafis.apps.ui.screen

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentTambahPenilaianBinding
import com.siafis.apps.utils.clearInput
import com.siafis.apps.utils.gone
import com.siafis.apps.utils.inputError
import com.siafis.apps.utils.visible
import java.io.Serializable


class TambahPenilaianFragment(private val id: String, private val gender: String) : BottomSheetDialogFragment() {

    private val binding: FragmentTambahPenilaianBinding by lazy {
        FragmentTambahPenilaianBinding.inflate(layoutInflater)
    }
    private var mediaPlayer:MediaPlayer? = null
    private lateinit var mBehavior: BottomSheetBehavior<*>
    private val multistage = listOf(7, 8, 8, 9, 9, 10, 10, 11, 11, 11, 12, 12, 13, 13, 13, 14, 14, 15, 15, 16)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(binding.root)

        setupUI()
        setupAction()

        return dialog
    }

    private fun setupUI() {
        mBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        mBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO

        binding.lytSpacer.minimumHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        showView(binding.appBarLayout, getActionBarSize())

        val itemsKategori = listOf(
            "Vertical Jump",
            "Hexagonal Obstacle Test",
            "Multistage Fitness Tes",
            "Hand Wall Toss",
            "Push Up",
            "Tes Akselerasi 35 Meter"
        )
        val adapterGender =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemsKategori)
        binding.etJenisPenilaian.setAdapter(adapterGender)
        binding.etJenisPenilaian.setText(itemsKategori[0], false)

        val itemsTingkat = mutableListOf<Int>()
        for (x in 1..20) {
            itemsTingkat.add(x)
        }
        val adaptertTingkat = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemsTingkat)
        binding.etTingkat.setAdapter(adaptertTingkat)
        binding.etTingkat.setText(itemsTingkat[0].toString(), false)

        binding.etNilai.clearInput(binding.nilai)
        binding.tingkat.gone()
        binding.btnAudio.gone()
        binding.etNilai.hint = "Hasil"
    }

    private fun setupAction() {
        binding.btClose.setOnClickListener { dismiss() }
        binding.etJenisPenilaian.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            if (binding.etJenisPenilaian.text.toString().trim() == "Multistage Fitness Tes") {
                binding.tingkat.visible()
                binding.btnAudio.visible()
                binding.etNilai.hint = "Bolak Balik"
            } else {
                binding.tingkat.gone()
                binding.btnAudio.gone()
                binding.etNilai.hint = "Hasil"
            }
        }
        binding.btnAudio.setOnClickListener {
           if(binding.btnAudio.text == resources.getString(R.string.mulai_audio)){
               mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beeptest).also {
                   it.start()
               }
               binding.btnAudio.text = resources.getString(R.string.stop_audio)
           }else{
               stopPlaying()
               binding.btnAudio.text = resources.getString(R.string.mulai_audio)
           }
        }
        binding.iconAction.setOnClickListener {
            val jenisTest = binding.etJenisPenilaian.text.toString().trim()
            val hasil = binding.etNilai.text.toString().trim()
            val tingkat = binding.etTingkat.text.toString().trim()
            val validation = arrayOfNulls<Boolean>(2)

            validation[0] = if (binding.tingkat.isGone) {
                true
            } else {
                if (binding.tingkat.inputError(
                        tingkat, resources.getString(
                            R.string.empty_fields,
                            "Tingkat atlet"
                        )
                    )
                ) {
                    if (tingkat.toInt() < 1) {
                        binding.tingkat.error = "Minimal tingkat 1"
                        false
                    } else {
                        true
                    }
                } else {
                    false
                }
            }
            validation[1] = if (binding.tingkat.isGone) {
                binding.nilai.inputError(
                    hasil, resources.getString(
                        R.string.empty_fields,
                        "Hasil Test"
                    )
                )
            } else {
                if (hasil.toInt() > multistage[tingkat.toInt()]) {
                    binding.nilai.error = "Hasil melebihi range penilaian"
                    false
                } else {
                    true
                }
            }

            if (!validation.contains(false)) {
                val xTingkat = if (binding.tingkat.isGone) {
                    0
                } else {
                    tingkat.toInt()
                }
                targetFragment?.onActivityResult(
                    targetRequestCode,
                    2,
                    Intent().putExtra(
                        "nilai",
                        setNilai(
                            id = id,
                            kategori = jenisTest,
                            gender = gender,
                            hasil = hasil.toDouble(),
                            tingkat = xTingkat
                        )
                    )
                )
                dismiss()
            }

        }
    }
    private fun stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
    private fun setNilai(
        id: String,
        kategori: String,
        gender: String,
        hasil: Double,
        tingkat: Int
    ): HashMap<String, Serializable> {
        val predikat = when (kategori) {
            "Vertical Jump" -> {
                when (gender) {
                    "Pria" -> {
                        when {
                            hasil > 70 -> {
                                "Excellent"
                            }
                            hasil in 61.0..70.0 -> {
                                "Sangat Baik"
                            }
                            hasil in 51.0..60.0 -> {
                                "Baik"
                            }
                            hasil in 41.0..50.0 -> {
                                "Cukup"
                            }
                            hasil in 31.0..40.0 -> {
                                "Sedang"
                            }
                            hasil in 21.0..30.0 -> {
                                "Kurang"
                            }
                            else -> "Buruk"
                        }
                    }
                    else -> {
                        when {
                            hasil > 60 -> {
                                "Excellent"
                            }
                            hasil in 51.0..60.0 -> {
                                "Sangat Baik"
                            }
                            hasil in 41.0..50.0 -> {
                                "Baik"
                            }
                            hasil in 31.0..40.0 -> {
                                "Cukup"
                            }
                            hasil in 21.0..30.0 -> {
                                "Sedang"
                            }
                            hasil in 11.0..20.0 -> {
                                "Kurang"
                            }
                            else -> "Buruk"
                        }
                    }

                }
            }
            "Hexagonal Obstacle Test" -> {
                when (gender) {
                    "Pria" -> {
                        when {
                            hasil < 11.2 -> {
                                "Sangat Baik"
                            }
                            hasil in 11.2..13.3 -> {
                                "Baik"
                            }
                            hasil in 13.4..15.5 -> {
                                "Cukup"
                            }
                            hasil in 15.6..17.8 -> {
                                "Kurang"
                            }
                            else -> "Kurang Sekali"
                        }
                    }
                    else -> {
                        when {
                            hasil < 12.2 -> {
                                "Sangat Baik"
                            }
                            hasil in 12.2..15.3 -> {
                                "Baik"
                            }
                            hasil in 15.4..18.5 -> {
                                "Cukup"
                            }
                            hasil in 18.6..21.8 -> {
                                "Kurang"
                            }
                            else -> "Kurang Sekali"
                        }
                    }
                }
            }
            "Multistage Fitness Tes" -> {
                when (tingkat) {
                    1 -> {
                        when (hasil) {
                            1.0 -> 17.2
                            2.0 -> 17.6
                            3.0 -> 18.0
                            4.0 -> 18.4
                            5.0 -> 18.8
                            6.0 -> 19.2
                            else -> 19.6
                        }
                    }
                    2 -> {
                        when (hasil) {
                            1.0 -> 20.0
                            2.0 -> 20.4
                            3.0 -> 20.8
                            4.0 -> 21.2
                            5.0 -> 21.6
                            6.0 -> 22.0
                            7.0 -> 22.4
                            else -> 22.8
                        }
                    }
                    3 -> {
                        when (hasil) {
                            1.0 -> 23.2
                            2.0 -> 23.6
                            3.0 -> 24.0
                            4.0 -> 24.4
                            5.0 -> 24.8
                            6.0 -> 25.2
                            7.0 -> 25.6
                            else -> 26.0
                        }
                    }
                    4 -> {
                        when (hasil) {
                            1.0 -> 26.4
                            2.0 -> 26.8
                            3.0 -> 27.2
                            4.0 -> 27.2
                            5.0 -> 27.6
                            6.0 -> 28.0
                            7.0 -> 28.7
                            8.0 -> 29.1
                            else -> 29.5
                        }
                    }
                    5 -> {
                        when (hasil) {
                            1.0 -> 29.8
                            2.0 -> 30.2
                            3.0 -> 30.6
                            4.0 -> 31.0
                            5.0 -> 31.4
                            6.0 -> 31.8
                            7.0 -> 32.4
                            8.0 -> 32.6
                            else -> 32.9
                        }
                    }
                    6 -> {
                        when (hasil) {
                            1.0 -> 33.2
                            2.0 -> 33.6
                            3.0 -> 33.9
                            4.0 -> 34.3
                            5.0 -> 34.7
                            6.0 -> 35.0
                            7.0 -> 35.4
                            8.0 -> 35.7
                            9.0 -> 36.0
                            else -> 36.4
                        }
                    }
                    7 -> {
                        when (hasil) {
                            1.0 -> 36.8
                            2.0 -> 37.1
                            3.0 -> 37.5
                            4.0 -> 37.5
                            5.0 -> 38.2
                            6.0 -> 38.5
                            7.0 -> 38.9
                            8.0 -> 39.2
                            9.0 -> 39.6
                            else -> 39.9
                        }
                    }
                    8 -> {
                        when (hasil) {
                            1.0 -> 40.2
                            2.0 -> 40.5
                            3.0 -> 40.8
                            4.0 -> 41.1
                            5.0 -> 41.5
                            6.0 -> 41.8
                            7.0 -> 42.0
                            8.0 -> 42.2
                            9.0 -> 42.6
                            10.0 -> 42.9
                            else -> 43.3
                        }
                    }
                    9 -> {
                        when (hasil) {
                            1.0 -> 43.6
                            2.0 -> 43.9
                            3.0 -> 44.2
                            4.0 -> 44.5
                            5.0 -> 44.9
                            6.0 -> 45.2
                            7.0 -> 45.5
                            8.0 -> 45.8
                            9.0 -> 46.2
                            10.0 -> 46.5
                            else -> 46.8
                        }
                    }
                    10 -> {
                        when (hasil) {
                            1.0 -> 47.1
                            2.0 -> 47.4
                            3.0 -> 47.7
                            4.0 -> 48.0
                            5.0 -> 48.4
                            6.0 -> 48.7
                            7.0 -> 49.0
                            8.0 -> 49.3
                            9.0 -> 49.6
                            10.0 -> 49.9
                            else -> 50.2
                        }
                    }
                    11 -> {
                        when (hasil) {
                            1.0 -> 50.5
                            2.0 -> 50.8
                            3.0 -> 51.1
                            4.0 -> 51.4
                            5.0 -> 51.6
                            6.0 -> 51.9
                            7.0 -> 52.2
                            8.0 -> 52.5
                            9.0 -> 52.8
                            10.0 -> 53.1
                            11.0 -> 53.4
                            else -> 53.7
                        }
                    }
                    12 -> {
                        when (hasil) {
                            1.0 -> 54.0
                            2.0 -> 54.3
                            3.0 -> 54.5
                            4.0 -> 54.8
                            5.0 -> 55.1
                            6.0 -> 55.4
                            7.0 -> 55.7
                            8.0 -> 56.0
                            9.0 -> 56.3
                            10.0 -> 56.5
                            11.0 -> 56.8
                            else -> 57.1
                        }
                    }
                    13 -> {
                        when (hasil) {
                            1.0 -> 57.4
                            2.0 -> 57.6
                            3.0 -> 57.9
                            4.0 -> 58.2
                            5.0 -> 58.5
                            6.0 -> 58.7
                            7.0 -> 59.0
                            8.0 -> 59.3
                            9.0 -> 59.5
                            10.0 -> 59.8
                            11.0 -> 60.0
                            12.0 -> 60.3
                            else -> 60.6
                        }
                    }
                    14 -> {
                        when (hasil) {
                            1.0 -> 60.8
                            2.0 -> 61.1
                            3.0 -> 61.4
                            4.0 -> 61.7
                            5.0 -> 62.0
                            6.0 -> 62.2
                            7.0 -> 62.5
                            8.0 -> 62.7
                            9.0 -> 63.0
                            10.0 -> 63.2
                            11.0 -> 63.5
                            12.0 -> 63.8
                            else -> 64.0
                        }
                    }
                    15 -> {
                        when (hasil) {
                            1.0 -> 64.3
                            2.0 -> 64.4
                            3.0 -> 64.8
                            4.0 -> 65.1
                            5.0 -> 65.3
                            6.0 -> 65.6
                            7.0 -> 65.9
                            8.0 -> 66.2
                            9.0 -> 66.5
                            10.0 -> 66.7
                            11.0 -> 66.9
                            12.0 -> 67.2
                            else -> 67.5
                        }
                    }
                    16 -> {
                        when (hasil) {
                            1.0 -> 67.8
                            2.0 -> 68.0
                            3.0 -> 68.3
                            4.0 -> 68.5
                            5.0 -> 68.8
                            6.0 -> 69.0
                            7.0 -> 69.3
                            8.0 -> 69.5
                            9.0 -> 69.7
                            10.0 -> 69.9
                            11.0 -> 70.2
                            12.0 -> 70.5
                            13.0 -> 70.7
                            else -> 70.9
                        }
                    }
                    17 -> {
                        when (hasil) {
                            1.0 -> 71.2
                            2.0 -> 71.4
                            3.0 -> 71.6
                            4.0 -> 71.9
                            5.0 -> 72.2
                            6.0 -> 72.4
                            7.0 -> 72.6
                            8.0 -> 72.9
                            9.0 -> 73.2
                            10.0 -> 73.4
                            11.0 -> 73.6
                            12.0 -> 73.9
                            13.0 -> 74.2
                            else -> 74.4
                        }
                    }
                    18 -> {
                        when (hasil) {
                            1.0 -> 74.6
                            2.0 -> 74.8
                            3.0 -> 75.0
                            4.0 -> 75.3
                            5.0 -> 75.6
                            6.0 -> 75.8
                            7.0 -> 76.0
                            8.0 -> 76.2
                            9.0 -> 76.5
                            10.0 -> 76.7
                            11.0 -> 76.9
                            12.0 -> 77.2
                            13.0 -> 77.4
                            14.0 -> 77.6
                            else -> 77.9
                        }
                    }
                    19 -> {
                        when (hasil) {
                            1.0 -> 78.1
                            2.0 -> 78.3
                            3.0 -> 78.5
                            4.0 -> 78.8
                            5.0 -> 79.0
                            6.0 -> 79.2
                            7.0 -> 79.5
                            8.0 -> 79.7
                            9.0 -> 79.9
                            10.0 -> 80.2
                            11.0 -> 80.4
                            12.0 -> 80.6
                            13.0 -> 80.8
                            14.0 -> 81.0
                            else -> 81.3
                        }
                    }
                    else -> {
                        when (hasil) {
                            1.0 -> 81.5
                            2.0 -> 81.8
                            3.0 -> 82.0
                            4.0 -> 82.2
                            5.0 -> 82.4
                            6.0 -> 82.6
                            7.0 -> 82.8
                            8.0 -> 83.0
                            9.0 -> 83.2
                            10.0 -> 83.5
                            11.0 -> 83.7
                            12.0 -> 83.9
                            13.0 -> 84.1
                            14.0 -> 84.3
                            15.0 -> 84.5
                            else -> 84.8
                        }
                    }
                }
            }
            "Hand Wall Toss" -> {
                when (gender) {
                    "Pria" -> {
                        when {
                            hasil > 35 -> {
                                "Baik Sekali"
                            }
                            hasil in 30.0..35.0 -> {
                                "Baik"
                            }
                            hasil in 20.0..29.0 -> {
                                "Sedang"
                            }
                            hasil in 15.0..19.0 -> {
                                "Kurang"
                            }
                            else -> "Kurang Sekali"
                        }
                    }
                    else -> {
                        when {
                            hasil > 35 -> {
                                "Baik Sekali"
                            }
                            hasil in 30.0..35.0 -> {
                                "Baik"
                            }
                            hasil in 20.0..29.0 -> {
                                "Sedang"
                            }
                            hasil in 15.0..19.0 -> {
                                "Kurang"
                            }
                            else -> "Kurang Sekali"
                        }
                    }
                }
            }
            "Push Up" -> {
                when (gender) {
                    "Pria" -> {
                        when {
                            hasil > 56 -> {
                                "Sangat Baik"
                            }
                            hasil in 47.0..56.0 -> {
                                "Baik"
                            }
                            hasil in 35.0..46.0 -> {
                                "Cukup"
                            }
                            hasil in 19.0..34.0 -> {
                                "Sedang"
                            }
                            hasil in 11.0..18.0 -> {
                                "Kurang"
                            }
                            hasil in 4.0..10.0 -> {
                                "Kurang Sekali"
                            }
                            else -> "Buruk"
                        }
                    }
                    else -> {
                        when {
                            hasil > 35 -> {
                                "Sangat Baik"
                            }
                            hasil in 27.0..35.0 -> {
                                "Baik"
                            }
                            hasil in 21.0..26.0 -> {
                                "Cukup"
                            }
                            hasil in 11.0..20.0 -> {
                                "Sedang"
                            }
                            hasil in 6.0..10.0 -> {
                                "Kurang"
                            }
                            hasil in 2.0..5.0 -> {
                                "Kurang Sekali"
                            }
                            else -> "Buruk"
                        }
                    }
                }
            }
            "Tes Akselerasi 35 Meter" -> {
                when (gender) {
                    "Pria" -> {
                        when {
                            hasil < 4.80 -> {
                                "Baik Sekali"
                            }
                            hasil in 4.80..5.09 -> {
                                "Baik"
                            }
                            hasil in 5.10..5.29 -> {
                                "Cukup"
                            }
                            hasil in 5.30..5.60 -> {
                                "Kurang"
                            }
                            else -> "Kurang Sekali"
                        }
                    }
                    else -> {
                        when {
                            hasil < 5.30 -> {
                                "Baik Sekali"
                            }
                            hasil in 5.30..5.59 -> {
                                "Baik"
                            }
                            hasil in 5.60..5.89 -> {
                                "Cukup"
                            }
                            hasil in 5.90..6.20 -> {
                                "Kurang"
                            }
                            else -> "Kurang Sekali"
                        }
                    }
                }
            }
            else -> "Not found"
        }
        val mapNilai = if (kategori == "Multistage Fitness Tes") {
            predikat
        } else {
            0
        }
        val mapPredikat = if (kategori == "Multistage Fitness Tes") {
            " "
        } else {
            predikat
        }
        return hashMapOf(
            "id" to id,
            "kategori" to kategori,
            "hasiltes" to hasil,
            "nilai" to mapNilai,
            "tingkat" to tingkat,
            "predikat" to mapPredikat
        )
    }

    override fun onStart() {
        super.onStart()
        mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val styledAttributes =
            requireContext().theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        return styledAttributes.getDimension(0, 0f).toInt()
    }

}