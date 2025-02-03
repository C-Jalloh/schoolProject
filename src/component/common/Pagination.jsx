import React from "react";
import '../../style/pagination.css'


const Pagination = ({currentPage, totalPage, onPageChange})=>{

    const pageNumbers = [];
    for( let i = 1; i <= totalPage; i++){
        pageNumbers.push(i)
    }

    return(
        <div className="pagination">
            {pageNumbers.map((number) => (
                <button key = {number}
                    onClick = {() => onPageChange(number)}
                    className= {number === currentPage ? 'active' : ''}>
                   {number}
                </button>
            ))}
        </div>
    )
}

export default Pagination;