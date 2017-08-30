package shop.fly.com.shop.mvp.view;


public interface IParentView {
	/**
	 * 显示提示框
	 */
	void showLoading();
	/**
	 * 关闭提示框
	 */
	void hideLoading();
	/**
	 * 无网提示
	 */
	void noNetWork();


	/**
	 * 错误提示
	 * @param str
	 */
	void showError(String str);

	/**
	 * 下拉没有更多数据
	 */
	void showNoNetWorkdate();
}
