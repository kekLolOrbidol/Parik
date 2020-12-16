package kristi.heroofmythhaven

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enterGame.setOnClickListener {
            val username = enterUsername.text
            if (username.isEmpty()) {
                Toast.makeText(this, "Fill in a username", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Username", username.toString())
                startActivity(intent)
            }
        }
    }
}
