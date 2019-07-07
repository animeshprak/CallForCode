package com.sample.dashboard

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ibm.callforcode.BuildConfig
import com.ibm.callforcode.R
import com.ibm.callforcode.listeners.OnEmployeeChangedTheStatus
import com.ibm.callforcode.webservice.data.Doc
import com.sample.listeners.OnEmployeeListItemClicked

class DashboardAdapter(
    private var mEmployeeDataList: List<Doc>,
    private val mOnEmployeeListItemClicked: OnEmployeeListItemClicked, private val mOnEmployeeStatusChanged: OnEmployeeChangedTheStatus)
    : RecyclerView.Adapter<DashboardAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employees_list_adapter, parent, false)
        return CharacterViewHolder(view)
    }

    override fun getItemCount() = mEmployeeDataList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val employeeData = mEmployeeDataList[position]
        if (employeeData != null) {
            holder.employeeName?.text = employeeData.empName
            holder.employeeId?.text = employeeData.id
            holder.employeeStatus?.text = Html.fromHtml(getStatus(employeeData.empInBuilding!!))
            holder.employeeDescription?.text = "Location - ${employeeData.seatNumber}, ODC - ${employeeData.odcNumber}, Floor - ${employeeData.odcFloor}"

            if (BuildConfig.LEVEL.equals("full_access")) {
                holder.employeeStatusChangerButton?.visibility = View.VISIBLE
                holder.employeeStatusChangerButton?.setOnClickListener {
                    employeeData.empInBuilding = !employeeData.empInBuilding!!
                    mOnEmployeeStatusChanged?.onStatusChanged(employeeData)
                    notifyDataSetChanged()
                }
            } else {
                holder.employeeStatusChangerButton?.visibility = View.GONE
            }
        }

        if (holder.employeeName?.context?.resources?.getBoolean(R.bool.isTablet)!! && position == 0) {
           // mOnEmployeeListItemClicked?.onCharacterItemClicked(characterData, holder.employeeName?.text.toString())
        }

        holder.itemView.setOnClickListener() {
            mOnEmployeeListItemClicked?.onEmployeeItemClicked(employeeData, holder.employeeName?.text.toString(), true)
        }
    }

    fun updateDataSet(updatedDataSet : List<Doc>) {
        mEmployeeDataList = updatedDataSet
        notifyDataSetChanged()
    }

    private fun getStatus(isEmployeeIn : Boolean) : String {
        var statusColor = "#CA0009"
        var statusText = "OUT"
        if (isEmployeeIn) {
            statusColor = "#008000"
            statusText = "IN"
        }
        return "<font color=#313C3F>Status</font> - <font color=$statusColor>$statusText</font>"
    }


    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var employeeName : TextView? = null
        var employeeId: TextView? = null
        var employeeStatus : TextView? = null
        var employeeDescription : TextView? = null
        var employeeStatusChangerButton : Button? = null
        init {
            employeeName = itemView.findViewById(R.id.employee_name)
            employeeId = itemView.findViewById(R.id.employee_id)
            employeeStatus = itemView.findViewById(R.id.employee_status)
            employeeDescription = itemView.findViewById(R.id.employee_description)
            employeeStatusChangerButton = itemView.findViewById(R.id.employee_status_changer_button)
        }
    }
}