using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PrsDbWebApi.Models;

namespace PrsDbWebApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RequestsController : ControllerBase 
    {
        private readonly AppDbContext _context;

        public RequestsController(AppDbContext context) {
            _context = context;
        }

        // GET: api/Requests
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Request>>> GetRequests() {
            return await _context.Requests.ToListAsync();
        }

        // GET: api/Requests/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Request>> GetRequest(int id) {
            var requests = await _context.Requests.FindAsync(id);

            if (requests == null) {
                return NotFound();
            }

            return requests;
        }

        [HttpGet("review/{userid}")]
        public async Task<ActionResult<Request>> RequestsInReviews(int userId) {

            var request = await _context.Requests
                                         .SingleOrDefaultAsync(x => x.Status == "REVIEW"
                                         && x.UserId != userId);





            return request;


        }







        // PUT: api/Requests/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutRequest(int id, Request request) {
            if (id != request.Id) {
                return BadRequest();
            }

            _context.Entry(request).State = EntityState.Modified;

            try {
                await _context.SaveChangesAsync();
            } catch (DbUpdateConcurrencyException) {
                if (!RequestExists(id)) {
                    return NotFound();
                } else {
                    throw;
                }
            }

            return NoContent();
        }


        [HttpPut("review/{requestid}")]
        public async Task<IActionResult> ReviewRequest(int requestId, Request request) {

            if (request.Total <= 50) {
                request.Status = "APPROVED";
            } else { request.Status = "REVIEW"; }

            return await PutRequest(requestId, request);

        }

        [HttpPut("approved/{requestid}")]
        public async Task<IActionResult> ApproveRequest(int requestId, Request request) {

            { request.Status = "APPROVED"; }

            return await PutRequest(requestId, request);


        }

        [HttpPut("rejected/{requestid}")]
        public async Task<IActionResult> RejectRequest(int requestId, Request request) {

            { request.Status = "REJECTED"; }

            return await PutRequest(requestId, request);

        }








        // POST: api/Requests
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Request>> PostRequest(Request request)
        {
            _context.Requests.Add(request);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetRequest", new { id = request.Id }, request);
        }

        // DELETE: api/Requests/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteRequest(int id)
        {
            var request = await _context.Requests.FindAsync(id);
            if (request == null)
            {
                return NotFound();
            }

            _context.Requests.Remove(request);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool RequestExists(int id)
        {
            return _context.Requests.Any(e => e.Id == id);
        }
    }
}
