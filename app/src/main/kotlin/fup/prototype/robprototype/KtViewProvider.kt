package fup.prototype.robprototype


interface KtViewProvider<in B, out LVM> {

    fun createViewModel(): LVM

    fun getLayoutId(): Int

    fun addViewListener()

    fun initBinding(binding: B)

}

