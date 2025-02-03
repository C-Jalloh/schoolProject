import React, {useEffect, useState} from "react";
import ApiService from "../../service/ApiService";
import { useNavigate, useParams } from "react-router-dom"; 
import '../../style/addProduct.css'



const EditProductPage = () => {
        const {productId} = useParams();
        const [image, setImage] = useState(null);
        const [categories, setCategories] = useState([]);
        const [categoryId, setCategoryId] = useState('')
        const [name, setName] = useState('')
        const [description, setDescription] = useState('')
        const [message, setMessage] = useState('')
        const [price, setPrice] = useState('')
        const [imageUrl, setImageUrl] = useState('')
        const navigate = useNavigate();


        useEffect(() =>{
            ApiService.getAllCategory().then((res) => setCategories(res.categoryList))

            if(productId) {
                ApiService.getProductById(productId).then((response) => {
                    setName(response.product.name);
                    setDescription(response.product.description);
                    setPrice(response.product.price);
                    setCategoryId(response.product.categoryId);
                    setImageUrl(response.product.imageUrl)
                })
            }
        },[productId]);

        const handleImageChange = (e) =>{
            // setImage(e.target.file[e]);
            console.log(e.target)
            setImageUrl(e.target.files[0].name)
        };

          const handleSubmit = async (e) => {
                e.preventDefault();
                try{
                    const formData = {id: productId, imageUrl: imageUrl, category: {id : categoryId}, name, description, price}
                    // if(image){
                    //     formData.append('image', image)
                    // }
                    // formData.append('productId', productId)
                    // formData.append('categoryId', categoryId)
                    // formData.append('name', name)
                    // formData.append('description', description)
                    // formData.append('price', price)
        
                    const response = await ApiService.updateProduct(productId,formData)
                    if(response.status === 200) {
                        setMessage(response.message)
                        setTimeout(() => {
                            setMessage('')
                            navigate('/admin/products')
                        },3000);
                    }
                }
               catch(error){
                setMessage (error.message?.data?.message || error.message || 'unable to update product')
               }
            }

            return(
                <form onSubmit={handleSubmit} className="product-form">
                    <h2>Edit Product</h2>
                    {message && <div className="message">{message}</div>}
                    <input type="file" onChange={handleImageChange} />
                    {imageUrl && <img src={imageUrl} alt={name}/>}
                    <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)}>
                        <option value="">Select Category</option>
                        {categories.map((cat) => (
                            <option value={cat.id} key={cat.id}>{cat.name}</option>
                        ))}
                    </select>

                    <input type="text"
               placeholder="Product name"
               value={name}
               onChange={(e) => setName(e.target.value)} />

               <textarea 
               placeholder="Description"
               value={description}
               onChange={(e) => setDescription(e.target.value)}>
               </textarea>

               <input type="number"
               placeholder="price"
               value={price}
               onChange={(e)=> setPrice(e.target.value)} />

               
               <button type="submit">Add Product</button>
                </form>
            )
        
}

export default EditProductPage