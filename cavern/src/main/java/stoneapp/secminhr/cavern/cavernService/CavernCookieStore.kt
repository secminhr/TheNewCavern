package stoneapp.secminhr.cavern.cavernService

import android.content.Context
import stoneapp.secminhr.cavern.database.CavernDatabase
import stoneapp.secminhr.cavern.database.CookieEntity
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI

class CavernCookieStore(context: Context): CookieStore {

    private val cookieDao = CavernDatabase.getInstance(context).cookieDao()
    private var cavernCookies = cookieDao.getAll() ?: listOf()

    override fun removeAll(): Boolean {
        cookieDao.delete(*(cavernCookies.toTypedArray()))
        cavernCookies = cookieDao.getAll() ?: listOf()
        return true
    }

    override fun remove(uri: URI?, cookie: HttpCookie?): Boolean {
        cookie?.let {
            cookieDao.findBy(uri, cookie)?.let {
                cookieDao.delete(it)
            }
        }
        throw NullPointerException()
    }

    override fun add(uri: URI?, cookie: HttpCookie?) {
        cookie?.let {
            cookieDao.insert(CookieEntity(uri, cookie.name, cookie.value, cookie.version))
            cavernCookies = cookieDao.getAll() ?: listOf()
        }
    }

    override fun getCookies(): MutableList<HttpCookie> {
        return cavernCookies.map { entity ->
            HttpCookie(entity.key, entity.value).also { it.version = entity.version }
        }.toMutableList()
    }

    override fun getURIs(): MutableList<URI> {
        return cavernCookies.mapNotNull { entity ->
            entity.uri
        }.toMutableList()
    }

    override fun get(uri: URI?): MutableList<HttpCookie> {
        return cavernCookies
                .filter { it.uri?.host == uri?.host }
                .map { entity ->
                    HttpCookie(entity.key, entity.value).also { it.version = entity.version }
                }
                .plus(HttpCookie("XSRF-TOKEN", XSRFTokenGenerator.token).also { it.version = 0 })
                .toMutableList()
    }
}