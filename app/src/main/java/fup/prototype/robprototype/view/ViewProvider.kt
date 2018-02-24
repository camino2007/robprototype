package fup.prototype.robprototype.view


interface ViewProvider<in B, out LVM> {

    fun createViewModel(): LVM?

    fun getLayoutId(): Int

    fun addViewListener()

    fun initBinding(binding: B?)

}

