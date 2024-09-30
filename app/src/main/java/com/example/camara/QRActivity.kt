package com.example.camara

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator

class QRActivity : AppCompatActivity() {

    private lateinit var codigoEditText: TextInputEditText
    private lateinit var descripcionEditText: TextInputEditText
    private lateinit var cantidadEditText: TextInputEditText
    private lateinit var precioEditText: TextInputEditText
    private lateinit var buttonScan: MaterialButton
    private lateinit var buttonAdd: MaterialButton
    private lateinit var buttonSearch: MaterialButton
    private lateinit var buttonClear: MaterialButton

    private val productos = arrayOfNulls<Producto>(10)
    private var productosCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qractivity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(getColor(R.color.white))
        supportActionBar?.title = "Leer QR"

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        toolbar.setNavigationIconTint(getColor(R.color.white))

        codigoEditText = findViewById(R.id.codigo_edit_text)
        descripcionEditText = findViewById(R.id.descripcion_edit_text)
        cantidadEditText = findViewById(R.id.cantidad_edit_text)
        precioEditText = findViewById(R.id.precio_edit_text)
        buttonScan = findViewById(R.id.button_scan)
        buttonAdd = findViewById(R.id.button_add)
        buttonSearch = findViewById(R.id.button_search)
        buttonClear = findViewById(R.id.button_clear)

        buttonScan.setOnClickListener { escanearCodigo() }
        buttonAdd.setOnClickListener { agregarProducto() }
        buttonSearch.setOnClickListener { buscarProducto() }
        buttonClear.setOnClickListener { limpiarCampos() }

        // Verificar permisos de cámara
        verificarPermisosDeCamara()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu?.let {
            for (i in 0 until it.size()) {
                val item = it.getItem(i)
                item.icon?.setTint(getColor(R.color.white))
            }
        }
        return true
    }

    private fun Toolbar.setNavigationIconTint(color: Int) {
        navigationIcon?.setTint(color)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bacK_button -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun escanearCodigo() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Coloca el código QR dentro del marco")
        integrator.setBeepEnabled(true)  // Habilitar sonido
        integrator.setBarcodeImageEnabled(true)  // Guardar la imagen del código escaneado
        integrator.initiateScan()  // Iniciar el escaneo
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                codigoEditText.setText(result.contents)  // Establecer el contenido escaneado
            } else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun agregarProducto() {
        val codigo = codigoEditText.text.toString()
        val descripcion = descripcionEditText.text.toString()
        val cantidad = cantidadEditText.text.toString().toIntOrNull() ?: 0
        val precio = precioEditText.text.toString().toDoubleOrNull() ?: 0.0

        if (codigo.isNotEmpty() && productosCount < productos.size && cantidad > 0 && precio > 0 && descripcion.isNotEmpty()) {
            val producto = Producto(codigo, descripcion, cantidad, precio)
            productos[productosCount] = producto
            productosCount++
            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "No se pudo agregar el producto, verifica los campos o ya no hay espacio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarProducto() {
        val codigoBuscado = codigoEditText.text.toString()
        val productoEncontrado = productos.firstOrNull { it?.codigo == codigoBuscado }

        if (productoEncontrado != null) {
            mostrarProducto(productoEncontrado)
        } else {
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarProducto(producto: Producto) {
        codigoEditText.setText(producto.codigo)
        descripcionEditText.setText(producto.descripcion)
        cantidadEditText.setText(producto.cantidad.toString())
        precioEditText.setText(producto.precio.toString())
    }

    private fun limpiarCampos() {
        codigoEditText.text?.clear()
        descripcionEditText.text?.clear()
        cantidadEditText.text?.clear()
        precioEditText.text?.clear()
    }

    private fun verificarPermisosDeCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }
}
